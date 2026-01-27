package com.example.College_Management_Portal.Config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(){

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        return RedisCacheConfiguration.defaultCacheConfig()
               .entryTtl(Duration.ofMinutes(15))
               .disableCachingNullValues()
               .serializeValuesWith(
                  RedisSerializationContext.SerializationPair
                  .fromSerializer(serializer));
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory){
        return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheConfiguration())
        .build();
    }

}
