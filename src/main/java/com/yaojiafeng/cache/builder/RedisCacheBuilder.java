package com.yaojiafeng.cache.builder;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yaojiafeng on 2018/12/3 7:14 PM.
 */
public class RedisCacheBuilder implements CacheBuilder {

    private String redisHost;
    private Integer redisPort;
    private Integer redisTimeout;
    private String redisPassword;
    private Integer redisDatabase;
    private int redisPoolConfigMaxTotal;

    public RedisCacheBuilder(String redisHost, Integer redisPort, Integer redisTimeout, String redisPassword, Integer redisDatabase, int redisPoolConfigMaxTotal) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisTimeout = redisTimeout;
        this.redisPassword = redisPassword;
        this.redisDatabase = redisDatabase;
        this.redisPoolConfigMaxTotal = redisPoolConfigMaxTotal;
    }

    public JedisPool build() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisPoolConfigMaxTotal);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeout, redisPassword, redisDatabase);
        return jedisPool;
    }


}
