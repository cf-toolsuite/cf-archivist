package io.pivotal.cfapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.relational.core.query.Criteria;

class CommonCriteria {

    static Criteria dateRange(String columnName, LocalDate start, LocalDate end) {
        return Criteria
            .where(columnName).lessThanOrEquals(LocalDateTime.of(end, LocalTime.MAX))
            .and(columnName).greaterThan(LocalDateTime.of(start, LocalTime.MIDNIGHT));
    }
}
