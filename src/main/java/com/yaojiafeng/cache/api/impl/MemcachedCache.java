package com.yaojiafeng.cache.api.impl;

import com.yaojiafeng.cache.api.AbstractCache;
import com.yaojiafeng.cache.api.Cache;
import com.yaojiafeng.cache.enums.CacheEnum;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:39.
 */
public class MemcachedCache extends AbstractCache implements Cache {

    private static Logger logger = LoggerFactory.getLogger(MemcachedCache.class);

    // 默认5分钟缓存失效时间
    private static final int DEFAULT_CACHE_EXP = 60 * 5;

    private final MemcachedClient memcachedClient;

    private final String appName;


    public MemcachedCache(MemcachedClient memcachedClient, String appName) {
        this.memcachedClient = memcachedClient;
        this.appName = appName;
    }

    @Override
    public void set(String key, Object value, int exp) {
        try {
            if (value == null || key.length() >= 250) {
                return;
            }
            memcachedClient.set(appName + SEPERATOR + key, exp, value);
//            keysMap.putIfAbsent(key, DEFAULT_VALUE);
        } catch (Exception e) {
            logger.error("set memcached cache error,key : " + key, e);
        }
    }

    @Override
    public void set(String key, Object value) {
        set(key, value, DEFAULT_CACHE_EXP);
    }

    @Override
    public Object get(String key) {
        try {
            if (key == null || key.length() >= 250) {
                return null;
            }
            return memcachedClient.get(appName + SEPERATOR + key);
        } catch (Exception e) {
            logger.error("get memcached error,key: " + key, e);
            return null;
        }
    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier, int exp) {
        Object value = get(key);
        if (value == null) {
            value = supplier.get();
            if (value != null) {
                set(key, value, exp);
            }
        }
        return (T) value;
    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier) {
        return get(key, supplier, DEFAULT_CACHE_EXP);
    }

    @Override
    public Map<String, Object> getBulk(List<String> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                return null;
            }
            keys = keys.stream().map(key -> appName + SEPERATOR + key).collect(Collectors.toList());
            Map<String, Object> bulk = memcachedClient.getBulk(keys);
            if (bulk != null && !bulk.isEmpty()) {
                Map<String, Object> map = new HashMap<>(bulk.size());
                int preLen = (appName + SEPERATOR).length();
                bulk.forEach((key, value) -> {
                    map.put(key.substring(preLen), value);
                });
                return map;
            }
            return null;
        } catch (Exception e) {
            logger.error("getBulk memcached error,keys: " + keys, e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            if (key == null || key.length() >= 250) {
                return;
            }
            memcachedClient.delete(appName + SEPERATOR + key);
//            keysMap.remove(key);
        } catch (Exception e) {
            logger.error("delete memcached error,key: " + key, e);
        }
    }

    @Override
    public void set(String key, Object value, int exp, boolean async) {
        if (async) {
            executorService.execute(() -> set(key, value, exp));
        } else {
            set(key, value, exp);
        }
    }

    @Override
    public void set(String key, Object value, boolean async) {
        if (async) {
            executorService.execute(() -> set(key, value));
        } else {
            set(key, value);
        }
    }

    @Override
    public CacheEnum getCacheType() {
        return CacheEnum.MEMCACHED_CACHE;
    }

}
