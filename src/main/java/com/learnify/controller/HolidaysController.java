package com.learnify.controller;

import com.learnify.model.Holiday;
import com.learnify.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class HolidaysController {

    @Autowired
    private HolidayRepository holidayRepository;

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

        Iterable<Holiday> holidays = holidayRepository.findAll();
        List<Holiday> holidayList = StreamSupport.stream(holidays.spliterator(), false).
                collect(Collectors.toList());
        Holiday.Type[] types = Holiday.Type.values();
        for(Holiday.Type type: types){
            model.addAttribute(type.toString(),
                    holidayList.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList()));
        }
        return "holidays.html";
    }
}
