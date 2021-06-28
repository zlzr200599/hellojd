package com.example.hellojd.logistics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class LogisticConfiguration {
//    @Bean
//    public StatefulRediSearchConnection<String, String> myRedisConfig(){
//        RediSearchClient client = RediSearchClient.create(RedisURI.create("192.168.1.7", 6379)); // (1)
////        RediSearchClient client = RediSearchClient.create(RedisURI.create("39.105.195.204", 6379)); // (1)
////        RediSearchClient client = RediSearchClient.create(RedisURI.create("localhost", 6379)); // (1)
//
//        return client.connect();
//    }
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.1.7", 6379);
//        return new JedisConnectionFactory(config);
//    }

//    @Bean
//    public GenericObjectPool<StatefulRediSearchConnection<String, String>> myRedisConnectPoll(){
//        RediSearchClient client = RediSearchClient.create(RedisURI.create("192.168.1.7", 6379)); // (1)
//        GenericObjectPoolConfig<StatefulRediSearchConnection<String, String>> config = new GenericObjectPoolConfig<>(); // (1)
//        config.setMaxTotal(2);
//        return ConnectionPoolSupport.createGenericObjectPool(client::connect, config); // (2)
//    }

//    @Bean
//    public LogisticsTimerService myTimer(){
//        return new LogisticsTimerService();
//    }
    @Bean
    public LogisticsDataset myDataset(){
        return new LogisticsDataset();
    }
}

