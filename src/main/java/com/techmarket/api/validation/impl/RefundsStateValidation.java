package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.RefundsState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class RefundsStateValidation implements ConstraintValidator<RefundsState,Integer> {
    private boolean allowNull;

    @Override
    public void initialize(RefundsState constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer refundKind, ConstraintValidatorContext constraintValidatorContext) {
        if(refundKind == null && allowNull) {
            return true;
        }
        if(!Objects.equals(refundKind, UserBaseConstant.REFUNDS_KIND_PROCCESSED) &&
                !Objects.equals(refundKind, UserBaseConstant.REFUNDS_KIND_NOT_YET_PROCCESS)) {
            return false;
        }
        return true;
    }
}
