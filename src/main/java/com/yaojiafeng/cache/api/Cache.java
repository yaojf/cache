package com.yaojiafeng.cache.api;


import com.yaojiafeng.cache.enums.CacheEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:31.
 */
public interface Cache {

    Object DEFAULT_VALUE = new Object();

    String SEPERATOR = ":";

    void set(String key, Object value, int exp);

    void set(String key, Object value);

    Object get(String key);

    <T extends Serializable> T get(String key, Supplier<T> supplier, int exp);

    <T extends Serializable> T get(String key, Supplier<T> supplier);

    Map<String, Object> getBulk(List<String> keys);

    void delete(String key);

    List<String> keys();

    void clear();

    /**
     *
     * @param key
     * @param value
     * @param exp
     * @param async true：异步 false：同步
     */
    void set(String key, Object value, int exp, boolean async);

    /**
     *
     * @param key
     * @param value
     * @param async true：异步 false：同步
     */
    void set(String key, Object value, boolean async);

    CacheEnum getCacheType();
}
