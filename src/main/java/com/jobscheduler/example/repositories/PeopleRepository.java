package com.jobscheduler.example.repositories;

import com.jobscheduler.example.models.People;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends CrudRepository<People, Long> {
}
