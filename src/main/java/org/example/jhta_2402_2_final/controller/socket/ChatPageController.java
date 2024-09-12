package org.example.jhta_2402_2_final.controller.socket;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatPageController {

    @GetMapping("/socketTest")
    public String socketTest() {
        return "socketTest";
    }

}
