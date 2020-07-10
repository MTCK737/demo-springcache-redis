package com.example.config;


import com.example.bean.MathCalculator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class ConfigOfAop {

    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }

    //@Bean(name = "CacheManager")
    public CacheManager getCacheManager()
    {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<ConcurrentMapCache> caches = new ArrayList<>();

        // math cache
        ConcurrentMapCache mathCache = new ConcurrentMapCache("mathCache",
                CacheBuilder.newBuilder().expireAfterWrite(1000, TimeUnit
                        .MINUTES).maximumSize(1000).build().asMap(), false);
        caches.add(mathCache);

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @SuppressWarnings("rawtypes")
    @Bean(name = "prettyGirl")
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(100));

        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();

        return cacheManager;
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer替换默认的序列化规则
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);


        //设置value的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //设置key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
