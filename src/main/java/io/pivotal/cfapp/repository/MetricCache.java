package io.pivotal.cfapp.repository;

import org.springframework.stereotype.Component;

import io.pivotal.cfapp.domain.SnapshotDetail;
import io.pivotal.cfapp.domain.TimeKeepers;
import lombok.Data;

@Data
@Component
public class MetricCache {

    private TimeKeepers timeKeepers;
    private SnapshotDetail snapshotDetail;

}
