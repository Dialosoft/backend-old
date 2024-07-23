package com.biznetbb.auth.web.controller;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznetbb.auth.persistence.response.ResponseBody;
import com.biznetbb.auth.service.ValidateService;

@RestController
@RequestMapping("/auth")
public class ValidatorController {

    @Autowired
    private ValidateService validator;

    public ResponseEntity<ResponseBody> validate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        return this.validator.validate(token);
    }
}
