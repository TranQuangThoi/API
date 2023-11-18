package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.OrderState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class OrderStateValidation implements ConstraintValidator<OrderState,Integer> {
    private boolean allowNull;

    @Override
    public void initialize(OrderState constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer state, ConstraintValidatorContext constraintValidatorContext) {
        if(state == null && allowNull) {
            return false;
        }
        if(!Objects.equals(state, UserBaseConstant.ORDER_STATE_CANCELED) &&
                !Objects.equals(state, UserBaseConstant.ORDER_STATE_PENDING_CONFIRMATION)&&
                !Objects.equals(state, UserBaseConstant.ORDER_STATE_COMPLETED)&&
                !Objects.equals(state, UserBaseConstant.ORDER_STATE_CONFIRMED)

        ) {
            return false;
        }
        return true;
    }
}
