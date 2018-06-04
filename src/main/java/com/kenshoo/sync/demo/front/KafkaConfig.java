package com.kenshoo.sync.demo.front;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${bootstrap.kafka.servers}")
    private String servers;


    @Bean
    public ProducerFactory<String, DownloadRequest> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                servers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        JsonSerializer<DownloadRequest> serializer = new JsonSerializer<>();
        serializer.setAddTypeInfo(false);
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
                serializer);
    }

    @Bean
    public KafkaTemplate<String, DownloadRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, DownloadResponse> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                servers);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(DownloadResponse.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DownloadResponse> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, DownloadResponse> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

//    @Bean JedisConnectionFactory jedisConnectionFactory() {
//
//        return new JedisConnectionFactory();
//
//    }
//
//    @Bean
//
//    public RedisTemplate<String, Object> redisTemplate() {
//
//        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//
//        template.setConnectionFactory(jedisConnectionFactory());
//
//        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//
//        return template;
//
//    }

}
