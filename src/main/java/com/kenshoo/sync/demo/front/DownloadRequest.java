package com.kenshoo.sync.demo.front;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author rbezpalko
 * @since 25.05.18
 */
@Getter
@Setter
class DownloadRequest {
    private String jobId;
    private String token;
    private List<String> ids;
}
