package io.pivotal.cfapp.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.config.PasSettings;
import io.pivotal.cfapp.domain.Space;
import io.pivotal.cfapp.event.AppDetailReadyToBeRetrievedEvent;
import io.pivotal.cfapp.event.AppDetailRetrievedEvent;
import io.pivotal.cfapp.service.AppDetailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppDetailTask implements ApplicationListener<AppDetailReadyToBeRetrievedEvent> {

    private final PasSettings settings;
    private final AppDetailService appDetailsService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public AppDetailTask(
            PasSettings settings,
            AppDetailService appDetailsService,
            ApplicationEventPublisher publisher) {
        this.settings = settings;
        this.appDetailsService = appDetailsService;
        this.publisher = publisher;
    }

    public void collect(List<Space> spaces) {
        log.info("AppDetailTask started");
        appDetailsService.
            .flatMap(appDetailsService::save)
            .thenMany(appDetailsService.findAll())
            .collectList()
            .subscribe(
                result -> {
                    publisher.publishEvent(new AppDetailRetrievedEvent(this).detail(result));
                    log.info("AppDetailTask completed");
                },
                error -> {
                    log.error("AppDetailTask terminated with error", error);
                }
            );
    }

    @Override
    public void onApplicationEvent(AppDetailReadyToBeRetrievedEvent event) {
        if (appDetailReadyToBeCollectedDecider.isDecided()) {
            collect(List.copyOf(appDetailReadyToBeCollectedDecider.getSpaces()));
            appDetailReadyToBeCollectedDecider.reset();
        }
    }

}
