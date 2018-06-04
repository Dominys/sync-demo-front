package com.kenshoo.sync.demo.front;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author rbezpalko
 * @since 24.05.18
 */
@RestController
@Slf4j
public class RequestController {

    @Autowired
    private KafkaTemplate<String, DownloadRequest> kafkaTemplate;
    @Autowired
    private RedisStatusStore redisStatusStore;


    @PostMapping("/download")
    public void convertCurrency(@RequestBody DownloadRequest request){
        log.info("Start job {} with ids {}", request.getJobId(), request.getIds());
        redisStatusStore.startJob(request);
        Lists.partition(request.getIds(), 20)
                .stream()
                .map(f -> {
                    DownloadRequest dR = new DownloadRequest();
                    dR.setIds(f);
                    dR.setJobId(request.getJobId());
                    dR.setToken(request.getToken());
                    return dR;
                })
                .forEach(this::sendRequestData);
    }

    public void sendRequestData(DownloadRequest request) {
        kafkaTemplate.send("download-request", request);
    }

}
