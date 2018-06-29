/**
 * 
 */
package com.cookienats.common.papimq.common.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
      
    @Bean(name= "redis.pool")
    @Autowired
    public JedisPool redisPool(@Qualifier("redis.pool.config") JedisPoolConfig config,
                @Value("${redis.host}")String host,
                @Value("${redis.port}")int port) {
        return new JedisPool(config, host, port);
    }
      
    @Bean(name= "redis.pool.config")
    public JedisPoolConfig redisPoolConfig (@Value("${redis.pool.maxTotal}")int maxTotal,
                                @Value("${redis.pool.maxIdle}")int maxIdle,
                                @Value("${redis.pool.maxWaitMillis}")int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }
}