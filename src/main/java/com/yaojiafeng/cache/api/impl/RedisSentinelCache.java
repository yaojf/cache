package com.yaojiafeng.cache.api.impl;

import com.yaojiafeng.cache.api.AbstractCache;
import com.yaojiafeng.cache.api.Cache;
import com.yaojiafeng.cache.enums.CacheEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by yaojiafeng on 2018/11/21 10:42 AM.
 */
public class RedisSentinelCache extends AbstractCache implements Cache {

    private static Logger logger = LoggerFactory.getLogger(RedisSentinelCache.class);

    /**
     * 应用名
     */
    private final String appName;

    /**
     * 连接池
     */
    private final JedisSentinelPool jedisSentinelPool;

    public RedisSentinelCache(String appName, JedisSentinelPool jedisSentinelPool) {
        this.appName = appName;
        this.jedisSentinelPool = jedisSentinelPool;
    }

    @Override
    public void set(String key, Object value, int exp) {
        Jedis jedis = null;
        try {
            if (value == null) {
                return;
            }
            jedis = jedisSentinelPool.getResource();
            jedis.setex(appName + SEPERATOR + key, exp, (String) value);
        } catch (Exception e) {
            logger.error("RedisSentinelCache set error,key: " + key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void set(String key, Object value) {
        Jedis jedis = null;
        try {
            if (value == null) {
                return;
            }
            jedis = jedisSentinelPool.getResource();
            jedis.set(appName + SEPERATOR + key, (String) value);
        } catch (Exception e) {
            logger.error("RedisSentinelCache set error,key: " + key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Object get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            return jedis.get(appName + SEPERATOR + key);
        } catch (Exception e) {
            logger.error("RedisSentinelCache get error,key: " + key, e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier, int exp) {
        return null;
    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier) {
        return null;
    }

    @Override
    public Map<String, Object> getBulk(List<String> keys) {
        return null;
    }

    @Override
    public void delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            jedis.del(appName + SEPERATOR + key);
        } catch (Exception e) {
            logger.error("RedisSentinelCache delete error,key: " + key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void set(String key, Object value, int exp, boolean async) {

    }

    @Override
    public void set(String key, Object value, boolean async) {

    }

    @Override
    public CacheEnum getCacheType() {
        return CacheEnum.REDIS_SENTINEL_CACHE;
    }

    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            return jedis.incr(appName + SEPERATOR + key);
        } catch (Exception e) {
            logger.error("RedisSentinelCache incr error,key: " + key, e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void expire(final String key, final int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            jedis.expire(appName + SEPERATOR + key, seconds);
        } catch (Exception e) {
            logger.error("RedisSentinelCache expire error,key: " + key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 简单分布式锁
     *
     * @param key
     * @param value
     * @return OK：成功 null：失败
     */
    public String setnx(final String key, final String value, final Integer expr) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            return jedis.set(appName + SEPERATOR + key, value, "NX", "EX", expr.longValue());
        } catch (Exception e) {
            logger.error("RedisSentinelCache setnx error,key: " + key, e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            return jedis.decr(appName + SEPERATOR + key);
        } catch (Exception e) {
            logger.error("RedisSentinelCache decr error,key: " + key, e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
