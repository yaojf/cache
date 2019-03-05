# 简单的缓存工具类封装

1. 配置参数参考config.properties

	spring.application.name参数方便不同的应用key监控

2. 配置xml参考cache.xml

3. 测试启动App类

	在test目录下启动App类查看结果

4. 设计思路

	* 适配不同的缓存中间件
	* 屏蔽内部连接细节
	* 如果有其他的缓存中间件，新增Cache实现，并在CacheConfig新增连接代码，设置进CacheTemplate做成单例
	* 目前支持memcached，redis sentinel ，redis 单机 ，本地JVM缓存

