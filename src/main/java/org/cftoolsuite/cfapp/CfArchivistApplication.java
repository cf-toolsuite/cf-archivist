package org.cftoolsuite.cfapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
@Theme(themeClass = Material.class, variant = Material.DARK)
@PWA(name = "A dashboard for displaying/filtering time-stamped snapshots over successive pulls from a cf-hoover instance", shortName = "cf-archivist")
public class CfArchivistApplication implements AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        SpringApplication.run(CfArchivistApplication.class, args);
    }

}
