package io.pivotal.cfapp.repository;

import org.springframework.stereotype.Component;

import io.pivotal.cfapp.domain.SnapshotDetail;
import lombok.Data;

@Data
@Component
public class MetricCache {

    private SnapshotDetail snapshotDetail;

}
