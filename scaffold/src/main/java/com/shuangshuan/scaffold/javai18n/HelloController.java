package com.shuangshuan.scaffold.javai18n;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class HelloController {
    // Steps:
    // --Understand Locale(know language codes)
    // --Configure Locale resolver and interceptor in spring
    // -- Create languge files(messages_XX.properties)  messages.properties:default
    // -- Update Thymeleaf views for i18n  Print text:[[#{key}]]  Attribute:th:value="#{key}"
    // -- Switch between languages(Englich,Chinese..)   URL paramter:?lang=xx

    @GetMapping("/")
    public String hello( HttpServletRequest request) {
        // 修改语言是在浏览器上修改，比如谷歌 设置  语言
        Locale currentLocal = request.getLocale();
        String countryCode = currentLocal.getCountry();
        String countryName = currentLocal.getDisplayCountry();
        String langCode = currentLocal.getLanguage();
        String langName = currentLocal.getDisplayLanguage();
        System.out.println(countryCode + ":" + countryName);
        System.out.println(langCode + ":" + langName);
        System.out.println("==========================");
        String[] languages = Locale.getISOLanguages();
        for (String language : languages) {
            Locale locale = new Locale(language);
            // de:德语 en:英语 zh:中文
            //      System.out.println(language+":"+locale.getDisplayLanguage());
        }
        return "hello";
    }

}
