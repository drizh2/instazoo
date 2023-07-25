package com.drizh2.instazoo.validations;

import com.drizh2.instazoo.annotations.PasswordMatches;
import com.drizh2.instazoo.payload.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) obj;
        return signupRequest.getPassword().equals(signupRequest.getConfirmPassword());
    }
}
