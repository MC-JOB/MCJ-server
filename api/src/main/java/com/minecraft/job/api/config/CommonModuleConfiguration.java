package com.minecraft.job.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.minecraft.job.common")
@EnableJpaRepositories(basePackages = {"com.minecraft.job.common"})
@ComponentScans({
        @ComponentScan(value = "com.minecraft.job.common"),
        @ComponentScan(value = "com.minecraft.job.integration")
})
@Configuration
public class CommonModuleConfiguration {
}
