package com.learnify.controller;

import com.learnify.model.Address;
import com.learnify.model.Person;
import com.learnify.model.Profile;
import com.learnify.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ProfileController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/displayProfile")
    public ModelAndView displayProfile(Model model, HttpSession session){
        Person person = (Person) session.getAttribute("loggedInUser");
        Profile profile = new Profile();

        profile.setName(person.getName());
        profile.setMobileNumber(person.getMobileNumber());
        profile.setEmail(person.getEmail());
        if(null != person.getAddress()){
            profile.setAddress1(person.getAddress().getAddress1());
            profile.setAddress2(person.getAddress().getAddress2());
            profile.setCity(person.getAddress().getCity());
            profile.setState(person.getAddress().getState());
            profile.setZipCode(person.getAddress().getZipCode());
        }
        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }

    @RequestMapping("/updateProfile")
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors,
                                HttpSession session){
        if(errors.hasErrors()){
            return "profile.html";
        }
        Person person = (Person) session.getAttribute("loggedInUser");
        person.setName(profile.getName());
        person.setMobileNumber(profile.getEmail());
        person.setEmail(profile.getEmail());

        if(person.getAddress() == null){
            person.setAddress(new Address());
        }

        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());

        this.personRepository.save(person);
        session.setAttribute("loggedInUser", person);
        return "redirect:/displayProfile";
    }
}
