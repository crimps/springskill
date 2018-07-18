package com.crimps.shiroskill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/perms")
public class PermsContorller {

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> perms(){
        Map<String, Object> resultMap = new HashMap<>();
        return resultMap;
    }

    @RequestMapping("test")
    @ResponseBody
    public Map<String, Object> test(){
        Map<String, Object> resultMap = new HashMap<>();
        return resultMap;
    }
}
