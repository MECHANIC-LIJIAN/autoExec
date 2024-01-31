package com.magic.ssh.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

//@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    private RedisProperties redisProperties;


    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration(RedisProperties properties){
        RedisProperties.Sentinel sentinel = properties.getSentinel();

        if (sentinel != null) {
            Set<RedisNode> redisNodeSet = new HashSet<>();
            properties.getSentinel().getNodes().forEach(x -> {
                redisNodeSet.add(new RedisNode(x.split(":")[0], Integer.parseInt(x.split(":")[1])));
              logger.info(x.split(":")[0] + "\t" + x.split(":")[1]);
            });

            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(sentinel.getMaster());
            config.setSentinels(redisNodeSet);
            config.setSentinelPassword(properties.getSentinel().getPassword());
//            config.setPassword(properties.getPassword());
            return config;
        }
        return null;
    }



    @Bean
    public JedisPoolConfig jedisPoolConfig(RedisProperties properties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxWait(properties.getJedis().getPool().getMaxWait());
        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig,RedisSentinelConfiguration redisSentinelConfiguration){
        return  new JedisConnectionFactory(redisSentinelConfiguration,jedisPoolConfig);
    }


    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setHashValueSerializer(new StringRedisSerializer());
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }


}