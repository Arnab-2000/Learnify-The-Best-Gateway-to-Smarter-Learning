package com.learnify.controller;

import com.learnify.model.Person;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping("student")
public class StudentController {

    @GetMapping("/displayCourses")
    public ModelAndView displayCoursesForStudent(Model model, HttpSession session){
        ModelAndView view = new ModelAndView("courses_enrolled.html");
        Person person = (Person) session.getAttribute("loggedInUser");
        view.addObject("person", person);
        return view;
    }
}
