package org.example.jhta_2402_2_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String home(){
        return "index/index";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }


}
