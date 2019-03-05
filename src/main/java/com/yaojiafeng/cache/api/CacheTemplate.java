package com.yaojiafeng.cache.api;

import com.yaojiafeng.cache.api.impl.LocalCache;
import com.yaojiafeng.cache.api.impl.MemcachedCache;
import com.yaojiafeng.cache.api.impl.RedisCache;
import com.yaojiafeng.cache.api.impl.RedisSentinelCache;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.CacheManager;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:18.
 */
public class CacheTemplate {

    private static String APP_NAME;

    private static Cache MEMCACHED_CACHE;

    private static Cache LOCAL_CACHE;

    private static Cache REDIS_SENTINEL_CACHE;

    private static Cache REDIS_CACHE;

    public static void registerMemcachedClient(MemcachedClient memcachedClient) {
        CacheTemplate.MEMCACHED_CACHE = new MemcachedCache(memcachedClient, APP_NAME);
    }

    public static void registerCacheManager(CacheManager cacheManager) {
        CacheTemplate.LOCAL_CACHE = new LocalCache(cacheManager);
    }

    public static Cache getMemcachedCache() {
        if (MEMCACHED_CACHE == null) {
            throw new IllegalStateException("MEMCACHED_CACHE未配置");
        }
        return MEMCACHED_CACHE;
    }

    public static Cache getLocalCache() {
        if (LOCAL_CACHE == null) {
            throw new IllegalStateException("LOCAL_CACHE未配置");
        }
        return LOCAL_CACHE;
    }

    public static void setAppName(String appName) {
        CacheTemplate.APP_NAME = appName;
    }

    public static void registerJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
        CacheTemplate.REDIS_SENTINEL_CACHE = new RedisSentinelCache(APP_NAME, jedisSentinelPool);
    }

    public static Cache getRedisSentinelCache() {
        if (REDIS_SENTINEL_CACHE == null) {
            throw new IllegalStateException("REDIS_SENTINEL_CACHE未配置");
        }
        return REDIS_SENTINEL_CACHE;
    }

    public static void registerJedisPool(JedisPool jedisPool) {
        CacheTemplate.REDIS_CACHE = new RedisCache(APP_NAME, jedisPool);
    }

    public static Cache getRedisCache() {
        if (REDIS_CACHE == null) {
            throw new IllegalStateException("REDIS_CACHE未配置");
        }
        return REDIS_CACHE;
    }
}
