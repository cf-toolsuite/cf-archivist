package org.cftoolsuite.cfapp.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor(access=AccessLevel.PACKAGE)
@NoArgsConstructor(access=AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Table("application_detail")
public class AppDetail {

	private String foundation;
	private String organization;
	private String space;
	private String appId;
	private String appName;
	private String buildpack;
	private String buildpackVersion;
	private String image;
	private String stack;
	@Default
	private Integer runningInstances = 0;
	@Default
	private Integer totalInstances = 0;
	@Default
	private Long memoryUsed = 0L;
	@Default
	private Long memoryQuota = 0L;
	@Default
	private Long diskUsed = 0L;
	@Default
	private Long diskQuota = 0L;
	@Default
	private List<String> urls = new ArrayList<>();
	private LocalDateTime lastPushed;
	private String lastEvent;
	private String lastEventActor;
	private LocalDateTime lastEventTime;
	private String buildpackReleaseType;
    private LocalDateTime buildpackReleaseDate;
    private String buildpackLatestVersion;
    private String buildpackLatestUrl;
	private String requestedState;
	@Column("collection_time")
	private LocalDateTime collectionDateTime;

	public String toCsv() {
        return String.join(",", wrap(getFoundation()), wrap(getOrganization()), wrap(getSpace()), wrap(getAppId()), wrap(getAppName()),
                wrap(getBuildpack()), wrap(getBuildpackVersion()), wrap(getImage()), wrap(getStack()), wrap(String.valueOf(getRunningInstances())),
                wrap(String.valueOf(getTotalInstances())), wrap(Double.toString(toMegabytes(getMemoryUsed()))), wrap(Double.toString(toMegabytes(getMemoryQuota()))),
                wrap(Double.toString(toMegabytes(getDiskUsed()))), wrap(Double.toString(toMegabytes(getDiskQuota()))),
                (wrap(String.join(",", getUrls() != null ? getUrls(): Collections.emptyList()))),
                wrap(getLastPushed() != null ? getLastPushed().toString() : ""), wrap(getLastEvent()),
                wrap(getLastEventActor()), wrap(getLastEventTime() != null ? getLastEventTime().toString() : ""),
                wrap(getRequestedState()),
                wrap(getBuildpackReleaseType()),
                wrap(getBuildpackReleaseDate() != null ? getBuildpackReleaseDate().toString() : ""),
                wrap(getBuildpackLatestVersion()),
                wrap(getBuildpackLatestUrl()));
    }

	private static String wrap(String value) {
		return value != null ? StringUtils.wrap(value, '"') : StringUtils.wrap("", '"');
	}

	private Double toMegabytes(Long input) {
		return Double.valueOf(input / Math.pow(1024, 2));
	}

	public Double getMemoryUsedInMb() {
		return toMegabytes(getMemoryUsed());
	}

	public Double getDiskUsedInMb() {
		return toMegabytes(getDiskUsed());
	}

	public Double getMemoryQuotaInMb() {
		return toMegabytes(getMemoryQuota());
	}

	public Double getDiskQuotaInMb() {
		return toMegabytes(getDiskQuota());
	}

	public String getUrlsAsCsv() {
		return String.join(",", getUrls() != null ? getUrls(): Collections.emptyList());
	}

	public static String headers() {
		return String.join(",", "foundation", "organization", "space", "application id", "application name", "buildpack", "buildpack version", "image",
                "stack", "running instances", "total instances", "memory used (in mb)", "memory quota (in mb)", "disk used (in mb)", "disk quota (in mb)", "urls", "last pushed", "last event",
                "last event actor", "last event time", "requested state",
                "latest buildpack release type", "latest buildpack release date", "latest buildpack version", "latest buildpack Url" );
	}

	public static AppDetailBuilder from(AppDetail detail) {
        return AppDetail
					.builder()
						.foundation(detail.getFoundation())
						.organization(detail.getOrganization())
						.space(detail.getSpace())
						.appId(detail.getAppId())
						.appName(detail.getAppName())
						.buildpack(detail.getBuildpack())
						.buildpackVersion(detail.getBuildpackVersion())
						.image(detail.getImage())
						.stack(detail.getStack())
						.runningInstances(detail.getRunningInstances())
						.totalInstances(detail.getTotalInstances())
						.memoryUsed(detail.getMemoryUsed())
						.memoryQuota(detail.getMemoryQuota())
						.diskUsed(detail.getDiskUsed())
						.diskQuota(detail.getDiskQuota())
						.urls(detail.getUrls())
						.lastPushed(detail.getLastPushed())
						.lastEvent(detail.getLastEvent())
						.lastEventActor(detail.getLastEventActor())
						.lastEventTime(detail.getLastEventTime())
						.buildpackReleaseType(detail.getBuildpackReleaseType())
						.buildpackReleaseDate(detail.getBuildpackReleaseDate())
						.buildpackLatestVersion(detail.getBuildpackLatestVersion())
						.buildpackLatestUrl(detail.getBuildpackLatestUrl())
						.requestedState(detail.getRequestedState())
						.collectionDateTime(detail.getCollectionDateTime());
	}
}
