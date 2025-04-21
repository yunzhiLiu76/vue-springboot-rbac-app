package com.shuangshuan.scaffold.nosql.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 设置 key-value 并设置过期时间（单位：秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置 key-value，没有超时
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取 key 对应的值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除 key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置 Hash 值
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 获取 Hash 值
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 删除 Hash 中的一个字段
     */
    public void hDelete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * 追加 List
     */
    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 弹出 List 元素
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 添加 Set 值
     */
    public void sAdd(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 移除 Set 值
     */
    public void sRemove(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 获取 Set 成员
     */
    public Object sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取有序集合成员
     */
    public Object zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 分布式锁 - 获取锁
     */
    public boolean tryLock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(0, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 分布式锁 - 释放锁
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 事务示例
     */
    public void transactionalSet(String key1, Object value1, String key2, Object value2) {
        redisTemplate.setEnableTransactionSupport(true);
        try {
            redisTemplate.multi();
            redisTemplate.opsForValue().set(key1, value1);
            redisTemplate.opsForValue().set(key2, value2);
            redisTemplate.exec();
        } catch (Exception e) {
            redisTemplate.discard();
            e.printStackTrace();
        } finally {
            redisTemplate.setEnableTransactionSupport(false);
        }
    }


    /**
     * 分页查询 Redis key-value
     * @param pattern 匹配 key 的模式，例如 "*" 查询所有，"user:*" 查询前缀为 user: 的 key
     * @param page 页码（从 1 开始）
     * @param pageSize 每页大小
     * @return Map<String, Object> 结果
     */
    public Map<String, Object> getAllKeysWithValues(String pattern, int page, int pageSize) {
        Set<String> allKeys = new HashSet<>();

        // 使用 scan 避免 keys * 性能问题
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(
                ScanOptions.scanOptions().match(pattern).count(1000).build())) {
            while (cursor.hasNext()) {
                allKeys.add(new String(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 排序并分页
        List<String> keyList = new ArrayList<>(allKeys);
        Collections.sort(keyList); // 排序，方便查询
        int total = keyList.size();
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        // 处理越界情况
        if (fromIndex >= total) {
            return Map.of("total", total, "data", Collections.emptyMap());
        }

        // 获取分页 key
        List<String> pagedKeys = keyList.subList(fromIndex, toIndex);

        // 获取对应 value
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : pagedKeys) {
            if(!key.startsWith("backup")){
                result.put(key, redisTemplate.opsForValue().get(key));
            }
        }

        return Map.of("total", total, "data", result);
    }
}
