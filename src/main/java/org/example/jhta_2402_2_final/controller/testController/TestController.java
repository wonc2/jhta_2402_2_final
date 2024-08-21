package org.example.jhta_2402_2_final.controller.testController;

import org.example.jhta_2402_2_final.exception.types.SampleException;
import org.example.jhta_2402_2_final.exception.types.TestException;
import org.example.jhta_2402_2_final.model.enums.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/error")
    public String testError(){
        throw new SampleException(ErrorCode.NOT_FOUND);
    }
}
