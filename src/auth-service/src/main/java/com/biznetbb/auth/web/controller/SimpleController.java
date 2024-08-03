package com.biznetbb.auth.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznetbb.auth.persistence.response.ResponseBody;

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
