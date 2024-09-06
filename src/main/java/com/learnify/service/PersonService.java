package com.learnify.service;

import com.learnify.constants.AppConstants;
import com.learnify.model.Person;
import com.learnify.model.Roles;
import com.learnify.repository.PersonRepository;
import com.learnify.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createPerson(Person person){
        boolean isSaved = false;
        Roles role = rolesRepository.findByRoleName(AppConstants.STUDENT_ROLE);
        person.setRoles(role);
        person.setPwd(this.passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if(null != person && person.getPersonId() > 0){
            isSaved = true;
        }
        return isSaved;
    }
}
