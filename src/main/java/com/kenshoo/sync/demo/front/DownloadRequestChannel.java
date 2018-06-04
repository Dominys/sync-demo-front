package com.kenshoo.sync.demo.front;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author rbezpalko
 * @since 24.05.18
 */
public interface DownloadRequestChannel {
    String OUTPUT = "download-request";

    @Output(OUTPUT)
    MessageChannel output();
}
