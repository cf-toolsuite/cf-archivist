package org.cftoolsuite.cfapp.domain;

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
@JsonPropertyOrder({ "foundation", "organization-id", "organization-name", "space-id", "space-name", "collection-date-time" })
@EqualsAndHashCode
@ToString
@Table("spaces")
public class Space {

    @JsonProperty("foundation")
    private final String foundation;

    @Column("org_id")
    @JsonProperty("organization-id")
    private final String organizationId;

    @Column("org_name")
    @JsonProperty("organization-name")
    private final String organizationName;

    @JsonProperty("space-id")
    private final String spaceId;

    @JsonProperty("space-name")
    private final String spaceName;

    @Column("collection_time")
    @JsonProperty("collection-date-time")
    private LocalDateTime collectionDateTime;

    @JsonCreator
    @PersistenceCreator
    Space(
            @JsonProperty("foundation") String foundation,
            @JsonProperty("organization-id") String organizationId,
            @JsonProperty("organization-name") String organizationName,
            @JsonProperty("space-id") String spaceId,
            @JsonProperty("space-name") String spaceName,
            @JsonProperty("collection-date-time") LocalDateTime collectionDateTime) {
        this.foundation = foundation;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.collectionDateTime = collectionDateTime;
    }

    public static SpaceBuilder from(Space space) {
        return Space
                .builder()
                    .foundation(space.getFoundation())
                    .organizationId(space.getOrganizationId())
                    .organizationName(space.getOrganizationName())
                    .spaceId(space.getSpaceId())
                    .spaceName(space.getSpaceName())
                    .collectionDateTime(space.getCollectionDateTime());
    }
}
