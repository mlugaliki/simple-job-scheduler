package com.jobscheduler.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JobSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSchedulerApplication.class, args);
    }

}
