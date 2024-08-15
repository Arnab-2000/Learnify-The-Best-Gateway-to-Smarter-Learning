package com.learnify.controller;

import com.learnify.model.Holiday;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HolidaysController {

    @GetMapping("/holidays/{display}")
    public String getHolidays(@PathVariable String display,
            Model model){

        if(display !=null && display.equals("all")){
            model.addAttribute("festival", true);
            model.addAttribute("federal", true);
        }else if(display!=null && display.equals("festival")){
            model.addAttribute("festival", true);
        }else if(display!=null && display.equals("federal")){
            model.addAttribute("federal", true);
        }

        List<Holiday> holidays = Arrays.asList(
                new Holiday("Jan 1", "New Year's Eve", Holiday.Type.FESTIVAL),
                new Holiday("Dec 25", "Christmas Eve", Holiday.Type.FESTIVAL),
                new Holiday("Jan 23", "Netaji's Birthday", Holiday.Type.FEDERAL),
                new Holiday("Sept 5", "Teacher's Day", Holiday.Type.FEDERAL),
                new Holiday("Nov 14", "Children's Day", Holiday.Type.FEDERAL),
                new Holiday("9 Oct", "Durga Puja", Holiday.Type.FESTIVAL)
        );
        Holiday.Type[] types = Holiday.Type.values();
        for(Holiday.Type type: types){
            model.addAttribute(type.toString(),
                    holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList()));
        }
        return "holidays.html";
    }
}
