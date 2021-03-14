//package com.struggle.sys.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//import java.util.List;
//
//@Service
//public class RedisService {
//
//    @Autowired
//    private ReactiveRedisTemplate reactiveRedisTemplate;
//
//
//    /**
//     *
//     * @param key
//     * @param value
//     */
//    public <T> void set(String key, T value) {
//        reactiveRedisTemplate.opsForValue().set(key, value);
//    }
//
//    /**
//     *
//     * @param key
//     * @param value
//     * @param expire 过期时间：单位默认分钟
//     */
//    public <T> void set(String key, T value, Long expire) {
//        reactiveRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(expire));
//    }
//
//
//    public <T> Mono<T> get(String key) {
//        Mono<T> mono = reactiveRedisTemplate.opsForValue().get(key);
//        return mono;
//    }
//
//
//    // 设置list
//    public void setRpush(Object key, Object value) {
//        reactiveRedisTemplate.opsForList().rightPush(key, value);
//    }
//
//    // 获取全部list的值
//    public List getRange(Object key) {
//        Flux<List> range = reactiveRedisTemplate.opsForList().range(key, 0, -1);
//        return range.blockFirst();
//    }
//
//    // 移除list指定的值: index>=0(删除与value相等的值)，index < 0 (删除所有的值)
//    public void lrem(Object key, Object value) {
//        reactiveRedisTemplate.opsForList().remove(key, 0, value);
//    }
//
//
//    /**
//     * 根据key删除
//     * @param key
//     */
//    public void delete(String key) {
//        reactiveRedisTemplate.delete(key);
//    }
//}
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


    public <T> T get(String key) {
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

    // 移除list指定的值: index>=0(删除与value相等的值)，index < 0 (删除所有的值)
    public void lrem(Object key, Object value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 判断指定Key是否过期
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据key删除
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
