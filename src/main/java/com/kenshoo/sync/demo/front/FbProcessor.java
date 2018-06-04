package com.kenshoo.sync.demo.front;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class FbProcessor {

    @Autowired
    private RedisStatusStore redisStatusStore;

    @KafkaListener(topics = "processed-ads", groupId = "processed-ads", containerFactory = "kafkaListenerContainerFactory")
    public void process(DownloadResponse response) {
        redisStatusStore.receiveResponce(response);
    }

}
