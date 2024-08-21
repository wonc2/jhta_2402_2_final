package org.example.jhta_2402_2_final.api.testApi;

import org.example.jhta_2402_2_final.exception.types.TestException;
import org.example.jhta_2402_2_final.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("api/test")
public class TestRestController {

    @ResponseBody
    @GetMapping("error")
    public ResponseEntity<Object> test() {
        throw new TestException(ErrorCode.BAD_REQUEST);
    }
}
