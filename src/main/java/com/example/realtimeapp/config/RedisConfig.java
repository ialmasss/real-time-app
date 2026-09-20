package com.example.realtimeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.ChannelTopic;
import com.example.realtimeapp.service.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;


@Configuration
public class RedisConfig {


    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;


    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chat-messages");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisMessageSubscriber, chatTopic());
        return container;
    }
}