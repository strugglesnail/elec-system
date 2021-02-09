package com.struggle.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *
     * @param key
     * @param value
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @param expire 过期时间：单位默认分钟
     */
    public <T> void set(String key, T value, Long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MINUTES);
    }

    
    public <T> T get(String key, Class<T> c) {
        return (T) redisTemplate.opsForValue().get(key);
    }


    // 设置list
    public void setRpush(Object key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    // 获取全部list的值
    
    public List getRange(Object key) {
        List range = redisTemplate.opsForList().range(key, 0, -1);
        return range;
    }

    // 移除list指定的值
    public void lrem(Object key, Long index, Object value) {
        redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 判断指定Key是否过期
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
