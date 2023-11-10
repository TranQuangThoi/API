package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.GenderKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class GenderKindValidation implements ConstraintValidator<GenderKind,Integer> {

    private boolean allowNull;
    @Override
    public void initialize(GenderKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer kind, ConstraintValidatorContext constraintValidatorContext) {
        if(kind == null && allowNull) {
            return true;
        }
        if(!Objects.equals(kind, UserBaseConstant.GENDER_KIND_MALE) &&
                !Objects.equals(kind, UserBaseConstant.GENDER_KIND_FEMALE) &&
                !Objects.equals(kind, UserBaseConstant.GENDER_KIND_OTHER)) {
            return false;
        }
        return true;
    }
}
