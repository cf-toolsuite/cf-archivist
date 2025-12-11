package org.cftoolsuite.cfapp.repository;

import org.cftoolsuite.cfapp.domain.SnapshotDetail;
import org.cftoolsuite.cfapp.domain.TimeKeepers;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MetricCache {

    private TimeKeepers timeKeepers;
    private SnapshotDetail snapshotDetail;

}
