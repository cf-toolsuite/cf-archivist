package io.pivotal.cfapp.repository;

import org.springframework.stereotype.Component;

import io.pivotal.cfapp.domain.Demographics;
import io.pivotal.cfapp.domain.SnapshotDetail;
import io.pivotal.cfapp.domain.SnapshotSummary;
import lombok.Data;

@Data
@Component
public class MetricCache {

    private SnapshotDetail snapshotDetail;
    private SnapshotSummary snapshotSummary;
    private Demographics demographics;

}
