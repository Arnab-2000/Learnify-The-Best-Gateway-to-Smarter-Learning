package com.learnify.controller;

import com.learnify.model.Course;
import com.learnify.model.LearnifyClass;
import com.learnify.model.Person;
import com.learnify.repository.CourseRepository;
import com.learnify.repository.LearnifyClassRepository;
import com.learnify.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private LearnifyClassRepository learnifyClassRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model){
        ModelAndView view = new ModelAndView("classes.html");
        List<LearnifyClass> classes = learnifyClassRepository.findAll();
        view.addObject("learnifyClass", new LearnifyClass());
        view.addObject("classes", classes);
        return view;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("learnifyClass") LearnifyClass learnifyClass){
        learnifyClassRepository.save(learnifyClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id){
        Optional<LearnifyClass> learnifyClass = this.learnifyClassRepository.findById(id);
        if(learnifyClass.isPresent()){
            for(Person person: learnifyClass.get().getPersons()){
                person.setLearnifyClass(null);
                this.personRepository.save(person);
            }
        }
        this.learnifyClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error){
        ModelAndView modelAndView = new ModelAndView("students.html");
        String errorMessage = null;
        Optional<LearnifyClass> learnifyClass = this.learnifyClassRepository.findById(classId);
        modelAndView.addObject("learnifyClass", learnifyClass.get());
        System.out.println("Class is: "+ learnifyClass.get().getPersons());
        modelAndView.addObject("person", new Person());
        session.setAttribute("learnifyClass", learnifyClass.get());
        if(null != error){
            errorMessage = "Invalid Email Entered !!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session){
        ModelAndView view = new ModelAndView();
        LearnifyClass learnifyClass = (LearnifyClass) session.getAttribute("learnifyClass");
        Person personEntity = this.personRepository.queryByEmail(person.getEmail());
        if(null == personEntity){
            view.setViewName("redirect:/admin/displayStudents?classId="+learnifyClass.getClassId()
            +"&error=true");
            return view;
        }
        personEntity.setLearnifyClass(learnifyClass);
//        personRepository.save(personEntity);
        learnifyClass.getPersons().add(personEntity);
        learnifyClassRepository.save(learnifyClass);
        view.setViewName("redirect:/admin/displayStudents?classId="+learnifyClass.getClassId());
        return view;
    }

    @RequestMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session){
        ModelAndView view = new ModelAndView();
        Optional<Person> person = this.personRepository.findById(personId);
        LearnifyClass learnifyClass = (LearnifyClass) session.getAttribute("learnifyClass");
        if(person.isPresent()){
            person.get().setLearnifyClass(null);
            learnifyClass.getPersons().remove(person.get());
            this.learnifyClassRepository.save(learnifyClass);
        }
        session.setAttribute("learnifyClass", learnifyClass);
        view.setViewName("redirect:/admin/displayStudents?classId="+learnifyClass.getClassId());
        return view;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayAllCourses(Model model){
        ModelAndView view = new ModelAndView("courses_secure.html");
        List<Course> courses = this.courseRepository.findAll();
        view.addObject("courses", courses);
        view.addObject("course", new Course());
        return view;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Course course){
        ModelAndView view = new ModelAndView();
        this.courseRepository.save(course);
        view.setViewName("redirect:/admin/displayCourses");
        return view;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam("id") int courseId, HttpSession session,
                                     @RequestParam(required = false) String error){
        String errorMessage = null;
        if(null != error){
            errorMessage = "Invalid Email Entered !!";
        }
        ModelAndView view = new ModelAndView("course_students.html");
        Optional<Course> course = this.courseRepository.findById(courseId);
        course.ifPresent(value -> view.addObject("courses", value));
        view.addObject("person", new Person());
        course.ifPresent(value -> session.setAttribute("course", value));
        view.addObject("errorMessage", errorMessage);
        return view;
    }
    
    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person,
                                           HttpSession session){
        ModelAndView view = new ModelAndView();
        Course course = (Course) session.getAttribute("course");
        Person personEntity = this.personRepository.queryByEmail(person.getEmail());
        if(null == personEntity){
            view.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId()+
                    "&error=true");
            return view;
        }
        personEntity.getCourses().add(course);
        course.getPersons().add(personEntity);
        this.personRepository.save(personEntity);
        session.setAttribute("course", course);
        view.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId());
        return view;
    }

    @RequestMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId,
                                                HttpSession session){

        ModelAndView view = new ModelAndView();
        Optional<Person> personEntity = this.personRepository.findById(personId);
        Course course = (Course) session.getAttribute("course");
        if(personEntity.isPresent()){
            course.getPersons().remove(personEntity.get());
            personEntity.get().getCourses().remove(course);
            this.personRepository.save(personEntity.get());
        }
        session.setAttribute("course", course);
        view.setViewName("redirect:/admin/viewStudents?id="+course.getCourseId());
        return view;
    }
}
