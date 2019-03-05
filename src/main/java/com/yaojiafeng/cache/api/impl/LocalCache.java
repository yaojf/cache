package com.yaojiafeng.cache.api.impl;

import com.yaojiafeng.cache.api.AbstractCache;
import com.yaojiafeng.cache.api.Cache;
import com.yaojiafeng.cache.enums.CacheEnum;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:37.
 */
public class LocalCache extends AbstractCache implements Cache {

    private static final String CACHE_NAME = "Default";

    private final CacheManager cacheManager;

    public LocalCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void set(String key, Object value, int exp) {
        throw new UnsupportedOperationException("LocalCache不支持过期");
    }

    @Override
    public void set(String key, Object value) {
        this.cacheManager.getCache(CACHE_NAME).put(key, value);
        keysMap.putIfAbsent(key, DEFAULT_VALUE);
    }

    @Override
    public Object get(String key) {
        return this.cacheManager.getCache(CACHE_NAME).get(key, Object.class);

    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier, int exp) {
        throw new UnsupportedOperationException("LocalCache不支持过期");
    }

    @Override
    public <T extends Serializable> T get(String key, Supplier<T> supplier) {
        Object value = get(key);
        if (value == null) {
            value = supplier.get();
            if (value != null) {
                set(key, value);
            }
        }
        return (T) value;
    }

    @Override
    public Map<String, Object> getBulk(List<String> keys) {
        throw new UnsupportedOperationException("LocalCache不支持批量查询");
    }

    @Override
    public void delete(String key) {
        this.cacheManager.getCache(CACHE_NAME).evict(key);
        keysMap.remove(key);
    }

    @Override
    public void set(String key, Object value, int exp, boolean async) {
        throw new UnsupportedOperationException("LocalCache不支持过期");
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
        return CacheEnum.LOCAL_CACHE;
    }

}
