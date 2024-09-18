package com.learnify.repository;

import com.learnify.model.Course;
import com.learnify.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByOrderByName();

    List<Course> findByOrderByNameDesc();
}
