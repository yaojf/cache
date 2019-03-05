package com.yaojiafeng.cache.config;

import com.yaojiafeng.cache.api.CacheTemplate;
import com.yaojiafeng.cache.builder.LocalCacheBuilder;
import com.yaojiafeng.cache.builder.MemcachedCacheBuilder;
import com.yaojiafeng.cache.builder.RedisCacheBuilder;
import com.yaojiafeng.cache.builder.RedisSentinelCacheBuilder;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.CacheManager;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Set;

/**
 * Created by yaojiafeng on 2018/9/25 下午7:17.
 */
public class CacheConfig implements InitializingBean, DisposableBean {

    /**
     * memcached
     */
    private boolean isMemcachedCacheEnable = false;

    /**
     * 本地缓存
     */
    private boolean isLocalCacheEnable = true;

    /**
     * 是否开启redis哨兵
     */
    private boolean isRedisSentinelCacheEnable = false;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * memcached服务器
     */
    private String memcachedCacheServers;

    /**
     * memcached服务器用户名
     */
    private String memcachedCacheUsername;

    /**
     * memcached服务器密码
     */
    private String memcachedCachePassword;

    /**
     * memcached客户端
     */
    private MemcachedClient memcachedClient;

    /**
     * 本地缓存
     */
    private CacheManager cacheManager;

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

    /**
     * 连接池
     */
    private JedisSentinelPool jedisSentinelPool;


    /**
     * redis单机配置
     */
    private boolean isRedisEnable;
    private JedisPool jedisPool;
    private String redisHost;
    private Integer redisPort;
    private Integer redisTimeout;
    private String redisPassword;
    private Integer redisDatabase;


    public void setIsMemcachedEnable(boolean isMemcachedEnable) {
        this.isMemcachedCacheEnable = isMemcachedEnable;
    }

    public boolean getIsMemcachedEnable() {
        return isMemcachedCacheEnable;
    }


    public void setIsLocalCacheEnable(boolean isLocalCacheEnable) {
        this.isLocalCacheEnable = isLocalCacheEnable;
    }

    public boolean getIsLocalCacheEnable() {
        return isLocalCacheEnable;
    }


    public void setIsRedisSentinelEnable(boolean isRedisSentinelEnable) {
        this.isRedisSentinelCacheEnable = isRedisSentinelEnable;
    }

    public boolean getIsRedisSentinelEnable() {
        return isRedisSentinelCacheEnable;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        checkConfig();
        initCache();
        register();
    }

    private void checkConfig() {
        if (StringUtils.isBlank(appName)) {
            throw new IllegalArgumentException("appName不能为空");
        }
        if (isMemcachedCacheEnable) {
            if (StringUtils.isBlank(memcachedCacheServers)) {
                throw new IllegalArgumentException("memcachedCacheServers不能为空");
            }
            if (StringUtils.isBlank(memcachedCacheUsername)) {
                throw new IllegalArgumentException("memcachedCacheUsername不能为空");
            }
            if (StringUtils.isBlank(memcachedCachePassword)) {
                throw new IllegalArgumentException("memcachedCachePassword不能为空");
            }
        }
        if (isRedisSentinelCacheEnable) {
            if (StringUtils.isBlank(redisMasterName)) {
                throw new IllegalArgumentException("redisMasterName不能为空");
            }
            if (redisSentinelsHost == null) {
                throw new IllegalArgumentException("redisSentinelsHost不能为空");
            }
        }
        if (isRedisEnable) {
            if (StringUtils.isBlank(redisHost)) {
                throw new IllegalArgumentException("redisHost不能为空");
            }
            if (redisPort == null) {
                throw new IllegalArgumentException("redisPort不能为空");
            }
            if (redisTimeout == null) {
                throw new IllegalArgumentException("redisTimeout不能为空");
            }
            if (redisDatabase == null) {
                throw new IllegalArgumentException("redisDatabase不能为空");
            }
        }
    }

    private void initCache() throws Exception {
        if (isMemcachedCacheEnable) {
            MemcachedClient memcachedClient = new MemcachedCacheBuilder().build(memcachedCacheServers, memcachedCacheUsername, memcachedCachePassword);
            this.memcachedClient = memcachedClient;
        }
        if (isLocalCacheEnable) {
            CacheManager cacheManager = new LocalCacheBuilder().build();
            this.cacheManager = cacheManager;
        }
        if (isRedisSentinelCacheEnable) {
            JedisSentinelPool jedisSentinelPool = new RedisSentinelCacheBuilder(redisMasterName, redisSentinelsHost, redisPoolConfigMaxTotal).build();
            this.jedisSentinelPool = jedisSentinelPool;
        }
        if (isRedisEnable) {
            JedisPool jedisPool = new RedisCacheBuilder(redisHost, redisPort, redisTimeout, redisPassword, redisDatabase, redisPoolConfigMaxTotal).build();
            this.jedisPool = jedisPool;
        }
    }

    private void register() {
        CacheTemplate.setAppName(appName);
        if (isMemcachedCacheEnable) {
            CacheTemplate.registerMemcachedClient(memcachedClient);
        }
        if (isLocalCacheEnable) {
            CacheTemplate.registerCacheManager(cacheManager);
        }
        if (isRedisSentinelCacheEnable) {
            CacheTemplate.registerJedisSentinelPool(jedisSentinelPool);
        }
        if (isRedisEnable) {
            CacheTemplate.registerJedisPool(this.jedisPool);
        }
    }


    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setMemcachedCacheServers(String memcachedCacheServers) {
        this.memcachedCacheServers = memcachedCacheServers;
    }

    public String getMemcachedCacheServers() {
        return memcachedCacheServers;
    }

    public void setMemcachedCacheUsername(String memcachedCacheUsername) {
        this.memcachedCacheUsername = memcachedCacheUsername;
    }

    public String getMemcachedCacheUsername() {
        return memcachedCacheUsername;
    }

    public void setMemcachedCachePassword(String memcachedCachePassword) {
        this.memcachedCachePassword = memcachedCachePassword;
    }

    public String getMemcachedCachePassword() {
        return memcachedCachePassword;
    }

    @Override
    public void destroy() throws Exception {
        // 释放资源连接
        if (this.memcachedClient != null) {
            memcachedClient.shutdown();
        }
        if (this.jedisSentinelPool != null) {
            this.jedisSentinelPool.close();
        }
    }

    public void setRedisMasterName(String redisMasterName) {
        this.redisMasterName = redisMasterName;
    }

    public String getRedisMasterName() {
        return redisMasterName;
    }

    public void setRedisSentinelsHost(Set<String> redisSentinelsHost) {
        this.redisSentinelsHost = redisSentinelsHost;
    }

    public Set<String> getRedisSentinelsHost() {
        return redisSentinelsHost;
    }

    public void setRedisPoolConfigMaxTotal(int redisPoolConfigMaxTotal) {
        this.redisPoolConfigMaxTotal = redisPoolConfigMaxTotal;
    }

    public int getRedisPoolConfigMaxTotal() {
        return redisPoolConfigMaxTotal;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public Integer getRedisTimeout() {
        return redisTimeout;
    }

    public void setRedisTimeout(Integer redisTimeout) {
        this.redisTimeout = redisTimeout;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(Integer redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public void setIsRedisEnable(boolean isRedisEnable) {
        this.isRedisEnable = isRedisEnable;
    }

    public boolean getIsRedisEnable() {
        return isRedisEnable;
    }
}
