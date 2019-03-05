package com.yaojiafeng.cache.api;

import com.yaojiafeng.cache.utils.NamedThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaojiafeng on 2018/9/26 上午11:31.
 */
public abstract class AbstractCache implements Cache {

    /**
     * 记录key集合
     */
    protected final ConcurrentHashMap keysMap = new ConcurrentHashMap();

    /**
     * 异步设置缓存
     */
    protected final ExecutorService executorService = Executors.newSingleThreadExecutor(new NamedThreadFactory(this.getCacheType().name()));

    @Override
    public List<String> keys() {
        return new ArrayList<>(keysMap.keySet());
    }

    @Override
    public void clear() {
        if (!keysMap.isEmpty()) {
            for (String key : keys()) {
                delete(key);
            }
        }
    }

}
