package com.hydata.intelligence.platform.cell_link.validation.phonevalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName PhoneValidationValidator
 * @Author pyt
 * @Date 2019/1/8 9:59
 */
public class PhoneValidationValidator implements ConstraintValidator<PhoneValidation, String> {
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^((13[0-9])|(15[0-9])|(18[0,2,3,5-9])|(17[0-8])|(147)|(16[0-9])|(19[0-9]))\\d{8}$"
    );

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.length() == 0) {
            return true;
        }
        Matcher m = PHONE_PATTERN.matcher(s);
        return m.matches();
    }

    @Override
    public void initialize(PhoneValidation constraintAnnotation) {

    }
}
