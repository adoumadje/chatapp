package com.adoumadje.chatapp.user.validation;

import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegistrationDto> {
    @Override
    public boolean isValid(UserRegistrationDto userRegistrationDto, ConstraintValidatorContext constraintValidatorContext) {
        if(userRegistrationDto.getPassword() == null || userRegistrationDto.getConfirmPassword() == null) {
            return false;
        }
        return userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword());
    }
}
