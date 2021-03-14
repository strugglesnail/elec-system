package com.struggle.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author strugglesnail
 * @date 2021/3/2
 * @desc
 */
@RestController
@RequestMapping("/flux")
public class FluxController {

    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @GetMapping("/1")
    public Mono<String> mono() {
        return Mono.just("flux");
    }

    @GetMapping("/2")
    public Flux<String> flux() {
        return Flux.just("1");
    }
}
