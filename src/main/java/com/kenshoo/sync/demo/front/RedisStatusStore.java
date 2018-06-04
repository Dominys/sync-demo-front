package com.kenshoo.sync.demo.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rbezpalko
 * @since 30.05.18
 */
@Component
@Slf4j
public class RedisStatusStore {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Resource(name="stringRedisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name="stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void startJob(DownloadRequest request) {
//        stringRedisTemplate.execute(new SessionCallback<Object>() {
//            @Override
//            public Object execute(RedisOperations operations) throws DataAccessException {
//                operations.multi();
//                operations.opsForSet().add("ads-" + request.getJobId(), request.getIds().toArray(new String[0]));
//                operations.opsForHash().put("jobStart", request.getJobId(), Long.toString(System.currentTimeMillis()));
//
//                return operations.exec();
//            }
//        });
        hashOperations.put("jobStart", request.getJobId(), Long.toString(System.currentTimeMillis()));
        setOperations.add("ads-" + request.getJobId(), request.getIds().toArray(new String[0]));
    }

    public void receiveResponce(DownloadResponse response) {
//        Long adsAwaitingCount = (Long) (stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
//            @Override
//            public List<Object> execute(RedisOperations operations) throws DataAccessException {
//                operations.multi();
//                operations.opsForSet().remove("ads-" + response.getJobId(), response.getId());
//                operations.opsForSet().size("ads-" + response.getJobId());
//                return operations.exec();
//            }
//        }).get(1));
//        log.info("Awaiting count {}", adsAwaitingCount);
//        if (adsAwaitingCount > 0) {
//            return;
//        }
//        hashOperations.put("jobEnd", response.getJobId(), Long.toString(System.currentTimeMillis()));
//        log.info("Job {} done!", response.getJobId());
        log.info("Responce N{}", atomicInteger.incrementAndGet());

        setOperations.remove("ads-" + response.getJobId(), response.getId());
        Long adsAwaitingCount = setOperations.size("ads-" + response.getJobId());
        log.info("Awaiting count {}", adsAwaitingCount);
        if (adsAwaitingCount > 0) {
            return;
        }
        hashOperations.put("jobEnd", response.getJobId(), Long.toString(System.currentTimeMillis()));
        log.info("Job {} done!", response.getJobId());
    }

}
