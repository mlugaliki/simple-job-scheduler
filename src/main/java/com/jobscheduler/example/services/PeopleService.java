package com.jobscheduler.example.services;

import com.jobscheduler.example.models.People;
import com.jobscheduler.example.repositories.PeopleRepository;
import com.jobscheduler.example.types.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    public List<People>getCustomers(){
       var people = (List<People>) peopleRepository.findAll();
       if(people!= null){
           return people.stream().filter(x -> x.getUserType()== UserType.CUSTOMER).collect(Collectors.toList());
       }

       return people;
    }

    public People getCustomerById(Long id) {
        return peopleRepository.findById(id).orElse(null);
    }
}
