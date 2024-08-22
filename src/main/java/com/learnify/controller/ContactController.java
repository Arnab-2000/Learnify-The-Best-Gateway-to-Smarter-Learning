package com.learnify.controller;


import com.learnify.model.Contact;
import com.learnify.service.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = {"/contact", "contacts"})
    public String displayContactPage(Model model){
        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

    @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
    public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors){

        if(errors.hasErrors()){
            log.error("Contact Form Validation Failed Due to : {}", errors.toString());
            return "contact.html";
        }

        contactService.saveMessageDetails(contact);
//        contactService.setCounter(contactService.getCounter()+1);
//        log.info("No.of times contact form is submitted : "+contactService.getCounter());
        return "redirect:/contact";
    }
    @RequestMapping("/displayMessages")
    public ModelAndView displayMessages(Model model){
        List<Contact> contactMsgsWithOpenStatus = contactService.findMsgWithOpenStatus();
        ModelAndView modelAndView = new ModelAndView("messages.html");
        modelAndView.addObject("contactMsgsWithOpenStatus", contactMsgsWithOpenStatus);
        return modelAndView;
    }

    @RequestMapping("/closeMsg")
    public String closeMessage(@RequestParam int id){
        contactService.updateMessageStatus(id);
        return "redirect:/displayMessages";
    }
}
