package com.crimps.shiroskill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/403")
    public String notPerms(){
        return "403";
    }
}
