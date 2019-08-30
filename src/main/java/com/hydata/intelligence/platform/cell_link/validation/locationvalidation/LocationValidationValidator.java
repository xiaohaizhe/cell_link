package com.hydata.intelligence.platform.cell_link.validation.locationvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName LocationValidationValidator
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/30 10:04
 * @Version
 */
public class LocationValidationValidator implements ConstraintValidator<LocationValidation, Float> {
    private Float max;
    private Float min;

    @Override
    public void initialize(LocationValidation constraintAnnotation) {
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        return value == null || (value >= min && value <= max);
    }
}
