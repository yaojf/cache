package com.yaojiafeng.cache;

import com.yaojiafeng.cache.api.CacheTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yaojiafeng on 2019/3/5 3:55 PM.
 */
public class App {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("cache.xml");
        classPathXmlApplicationContext.start();

        CacheTemplate.getRedisCache().set("key", "value");

        System.out.println(CacheTemplate.getRedisCache().get("key"));

        CacheTemplate.getRedisCache().delete("key");

    }
}
