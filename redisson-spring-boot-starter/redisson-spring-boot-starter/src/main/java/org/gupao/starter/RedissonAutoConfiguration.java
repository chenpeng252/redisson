package org.gupao.starter;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnClass(Redisson.class)
public class RedissonAutoConfiguration {

    @Bean
    RedissonClient redissonClient(RedissonProperties redissonProperties){
        Config config = new Config();
        String prefix = "redis://";
        if(redissonProperties.isSsl()){
            prefix = "rediss://";
        }
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(prefix + redissonProperties.getHost() +":"+ redissonProperties.getPort())
                .setConnectTimeout(redissonProperties.getTimeout());
        /**
         * Exception: Unable to decode data. channel...
         * 可以获取到项目当中新存入的数据，但是无法获取到昨天通过redis-cli存入的数据。
         * 错误信息提示：无法解码数据，猜测可能是redis-cli存数据的编码和redission的编码不一致导致的。
         * 最后网上找了很多的资料，找到了一个叫序列化策略的东西。
         * 大致的情况就是说redis-cli存数据时的序列化策略是string，
         * 但是redission的默认序列化策略是Jackson JSON 编码 Redission官网配置参数序列化说明
         * 故下面转化处理一下
         */
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }
}
