package com.jobscheduler.example.configurations;

import java.io.IOException;
import java.util.Properties;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public JobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setJobFactory(jobFactory());
        schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");
        //schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        return schedulerFactory;
    }

    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
