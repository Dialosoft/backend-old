package com.dialosoft.auth.web.controller;

import com.dialosoft.auth.persistence.response.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
public class SimpleController {

    @GetMapping
    public ResponseEntity<ResponseBody<?>> validate(){
        return ResponseEntity.ok(ResponseBody.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Hello world successful")
                .metadata(null)
                .build());
    }
}
