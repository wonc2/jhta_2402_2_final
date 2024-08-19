package org.example.jhta_2402_2_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("sample")
    public String sample(){
        return "sample.html";
    }
}
