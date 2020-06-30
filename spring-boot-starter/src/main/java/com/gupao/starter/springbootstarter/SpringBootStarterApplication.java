package com.gupao.starter.springbootstarter;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarterApplication.class, args);
    }

    @Autowired
    RedissonClient redissonClient;

    /**
     * Linux系统中redis服务器必须启动状态，不然项目启动报错
     * @return
     */
    @GetMapping("/say")
    public String say(){
        RBucket bucket = redissonClient.getBucket("name");
        if(bucket.get()==null){
            bucket.set("java.gupaoedu.com");
        }
        return bucket.get().toString();
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
