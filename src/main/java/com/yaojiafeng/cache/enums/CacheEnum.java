package com.yaojiafeng.cache.enums;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:24.
 */
public enum CacheEnum {
    LOCAL_CACHE,
    MEMCACHED_CACHE,
    REDIS_SENTINEL_CACHE,
    REDIS_CACHE;

    public static CacheEnum of(String name) {
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            if (cacheEnum.name().equals(name)) {
                return cacheEnum;
            }
        }
        return null;
    }

}
