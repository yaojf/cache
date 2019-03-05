package com.yaojiafeng.cache.builder;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

/**
 * Created by yaojiafeng on 2018/9/25 下午7:52.
 */
public class LocalCacheBuilder implements CacheBuilder {

    public CacheManager build(){
        return new ConcurrentMapCacheManager();
    }
}
