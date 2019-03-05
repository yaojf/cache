package com.yaojiafeng.cache.builder;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Set;

/**
 * Created by yaojiafeng on 2018/11/21 10:49 AM.
 */
public class RedisSentinelCacheBuilder implements CacheBuilder {

    /**
     * redis主机名
     */
    private String redisMasterName;

    /**
     * 哨兵主机群
     */
    private Set<String> redisSentinelsHost;

    /**
     * 连接池最大数量
     */
    private int redisPoolConfigMaxTotal;

    public RedisSentinelCacheBuilder(String redisMasterName, Set<String> redisSentinelsHost, int redisPoolConfigMaxTotal) {
        this.redisMasterName = redisMasterName;
        this.redisSentinelsHost = redisSentinelsHost;
        this.redisPoolConfigMaxTotal = redisPoolConfigMaxTotal;
    }

    public JedisSentinelPool build() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisPoolConfigMaxTotal);
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(redisMasterName, redisSentinelsHost, jedisPoolConfig);
        return jedisSentinelPool;
    }

}
