package com.learnify.controller;

import com.learnify.model.Person;
import com.learnify.service.PersonService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@RequestMapping("public")
public class PublicController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterPage(Model model){
        model.addAttribute("person", new Person());
        return "register.html";
    }

    @RequestMapping(value = "/createUser", method = {RequestMethod.POST})
    public String createUser(@Valid @ModelAttribute("person") Person person, Errors errors){
        if(errors.hasErrors()){
            return "register.html";
        }
        boolean isSaved = personService.createPerson(person);
        if(isSaved){
            return "redirect:/login?register=true";
        }
        return "register.html";
    }
}
