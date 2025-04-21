package com.shuangshuan.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
public class GatewayConfig {

    Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DiscoveryClient discoveryClient) {
        return builder.routes()
                // ✅ 直接转发 CA 认证相关请求
                .route("cryptauth_service", r -> r.path("/cryptauth/**")
                        .uri("lb://cryptauth"))

                // ✅ 业务 API 先经过 CA 认证，再决定是否转发
                .route("scaffold_service", r -> r.path("/scaffold/**")
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    // 🚀 获取 CA 服务的实际地址
                                    List<ServiceInstance> instances = discoveryClient.getInstances("cryptauth");
                                    if (instances.isEmpty()) {
                                        // 🚨 CA 不可用，返回 503
                                        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                                        return exchange.getResponse().setComplete();
                                    }
                                    String caUrl = instances.get(0).getUri().toString(); // 获取 CA 实际地址
                                    String authUrl = caUrl + "/cryptauth"+exchange.getRequest().getURI().getPath();

                                    WebClient webClient = WebClient.create();
                                    logger.info("Auth URL: {}", authUrl);
                                    logger.info("Request Headers: {}", exchange.getRequest().getHeaders());

                                    return webClient.get()
                                            .uri(authUrl)
                                            .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
                                            .exchangeToMono(response -> {
                                                if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) ||
                                                        response.statusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                                                    // 🚨 认证失败，返回 JSON 响应
                                                    exchange.getResponse().setStatusCode(response.statusCode());
                                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                                                    // 直接返回 JSON 对象
                                                    Map<String, Object> errorResponse = Map.of(
                                                            "code", 401,
                                                            "message", response.statusCode().equals(HttpStatus.UNAUTHORIZED) ?
                                                                    "Unauthorized - Token Invalid or Expired" :
                                                                    "Forbidden - Access Denied"
                                                    );

                                                    // 使用 ObjectMapper 转换为 JSON
                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                    byte[] jsonBytes;
                                                    try {
                                                        jsonBytes = objectMapper.writeValueAsBytes(errorResponse);
                                                    } catch (JsonProcessingException e) {
                                                        return Mono.error(e);
                                                    }

                                                    DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                                                    DataBuffer buffer = bufferFactory.wrap(jsonBytes);
                                                    return exchange.getResponse().writeWith(Mono.just(buffer));
                                                }
                                                // ✅ 认证通过，继续请求业务服务
                                                return chain.filter(exchange);
                                            });
                                })
                        )
                        .uri("lb://scaffold"))  // 🎯 转发到业务 API
                .build();
    }




}
