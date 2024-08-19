package org.example.jhta_2402_2_final.api;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SampleRestController {

    // api 컨트롤러 샘플

    private final SampleService sampleService;

    @GetMapping("index")
    public ResponseEntity<Object> index(@RequestParam Map params){
        Object response = (Map) sampleService.getTestApi(params);
        return ResponseEntity.ok().body(response);
    }
}
