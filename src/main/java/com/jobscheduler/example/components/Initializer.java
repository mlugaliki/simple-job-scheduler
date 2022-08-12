package com.jobscheduler.example.components;

import com.jobscheduler.example.models.People;
import com.jobscheduler.example.repositories.PeopleRepository;
import com.jobscheduler.example.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private PeopleRepository peopleRepository;

    @Override
    public void run(String... args) throws Exception {
        List users = List.of(
                new People(1L, "254722000000", "John", "Doe", "", UserType.CUSTOMER),
                new People(2L, "254722000001", "Obama", "Barak", "", UserType.CUSTOMER),
                new People(3L, "254722000002", "Joe", "Biden", "", UserType.CUSTOMER),
                new People(4L, "254722000003", "Mary", "Mary", "", UserType.STAFF)
        );

        peopleRepository.saveAll(users);
    }
}
