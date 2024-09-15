package com.learnify.controller;

import com.learnify.model.Contact;
import com.learnify.model.Person;
import com.learnify.repository.PersonRepository;
import com.learnify.service.ContactService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class DashboardController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session){
        Person person = this.personRepository.queryByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());

        if(null != person.getLearnifyClass()){
            model.addAttribute("enrolledClass", person.getLearnifyClass().getName());
        }
        session.setAttribute("loggedInUser", person);
        return "dashboard.html";
    }
}
