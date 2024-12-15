package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.PaymentKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PaymentKindValidation  implements ConstraintValidator<PaymentKind,Integer> {

    private boolean allowNull;
    @Override
    public void initialize(PaymentKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer kind, ConstraintValidatorContext constraintValidatorContext) {
        if(kind == null && allowNull) {
            return false;
        }
        if(!Objects.equals(kind, UserBaseConstant.PAYMENT_KIND_CASH) &&
                !Objects.equals(kind, UserBaseConstant.PAYMENT_KIND_BANK_TRANFER)&&
                !Objects.equals(kind, UserBaseConstant.PAYMENT_KIND_VNPAY)) {
            return false;
        }
        return true;
    }
}
