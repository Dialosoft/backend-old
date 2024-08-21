package com.dialosoft.auth.service.validator;

import com.dialosoft.auth.service.utils.AtLeastOneField;
import com.dialosoft.auth.web.dto.request.LoginRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, LoginRequest> {

    @Override
    public boolean isValid(LoginRequest loginRequest, ConstraintValidatorContext context) {
        return loginRequest.getUsername() != null;
        // TODO: Change this support both email and username
//        return loginRequest.getUsername() != null || loginRequest.getEmail() != null;
    }
}
