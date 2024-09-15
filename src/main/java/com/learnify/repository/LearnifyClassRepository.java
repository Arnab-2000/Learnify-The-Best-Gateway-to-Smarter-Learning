package com.learnify.repository;

import com.learnify.model.LearnifyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnifyClassRepository extends JpaRepository<LearnifyClass, Integer> {
}
