package com.learnify.repository;

import com.learnify.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person queryByEmail(String email);
}
