package com.thedevlair.business.courses.courses.service.impl;

import com.thedevlair.business.courses.courses.model.business.Course;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RedisServiceImpl {

    private final ReactiveRedisTemplate<String, Course> redisTemplate;

    @Value("${spring.redis.ttl.seconds}") // Puedes definir el tiempo de expiración en tu application.yml
    private long ttlSeconds;

    public RedisServiceImpl(ReactiveRedisTemplate<String, Course> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> put(String key, Course course) {
        Mono<Boolean> setResult = redisTemplate.opsForValue().set(key, course);

        // time duration
        setResult = setResult.flatMap(success -> {
            if (!Boolean.FALSE.equals(success)) {
                return redisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
            }
            return Mono.just(false);
        });

        return setResult;
    }

    public Mono<Course> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> delete(String key) {
        return redisTemplate.opsForValue().delete(key);
    }
}
