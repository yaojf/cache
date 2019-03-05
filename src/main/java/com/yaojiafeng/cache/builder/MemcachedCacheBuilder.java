package com.yaojiafeng.cache.builder;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.spring.MemcachedClientFactoryBean;

/**
 * Created by yaojiafeng on 2018/9/25 下午8:03.
 */
public class MemcachedCacheBuilder implements CacheBuilder {

    public MemcachedClient build(String servers, String username, String password) throws Exception {
        MemcachedClientFactoryBean memcachedClientFactoryBean = new MemcachedClientFactoryBean();
        memcachedClientFactoryBean.setServers(servers);
        memcachedClientFactoryBean.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        memcachedClientFactoryBean.setOpTimeout(1000L);
        PlainCallbackHandler plainCallbackHandler = new PlainCallbackHandler(username, password);
        AuthDescriptor authDescriptor = new AuthDescriptor(new String[]{"PLAIN"}, plainCallbackHandler);
        memcachedClientFactoryBean.setAuthDescriptor(authDescriptor);
        memcachedClientFactoryBean.afterPropertiesSet();
        return (MemcachedClient) memcachedClientFactoryBean.getObject();
    }

}
