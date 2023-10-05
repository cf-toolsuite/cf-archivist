package io.pivotal.cfapp.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@JsonPropertyOrder({ "foundation", "collection-date-time" })
@EqualsAndHashCode
@ToString
@Table("time_keeper")
public class TimeKeeper {

    @JsonProperty("foundation")
    private String foundation;

    @Column("collection_time")
    @JsonProperty("collection-date-time")
    private LocalDateTime collectionDateTime;

    @PersistenceCreator
    @JsonCreator
    TimeKeeper(@JsonProperty("foundation") String foundation,
        @JsonProperty("collection-date-time") LocalDateTime collectionDateTime) {
        this.foundation = foundation;
        this.collectionDateTime = collectionDateTime;
    }

}
