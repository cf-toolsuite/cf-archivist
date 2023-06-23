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
@EqualsAndHashCode
@JsonPropertyOrder({ "foundation", "id", "name", "collection-date-time" })
@ToString
@Table("organizations")
public class Organization {

    @JsonProperty("foundation")
    private String foundation;

    @JsonProperty("id")
    private final String id;

    @Column("org_name")
    @JsonProperty("name")
    private final String name;

    @Column("collection_time")
    @JsonProperty("collection-date-time")
    private LocalDateTime collectionDateTime;

    @JsonCreator
    @PersistenceCreator
    public Organization(
            @JsonProperty("foundation") String foundation,
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("collection-date-time") LocalDateTime collectionDateTime) {
        this.foundation = foundation;
        this.id = id;
        this.name = name;
        this.collectionDateTime = collectionDateTime;
    }

}
