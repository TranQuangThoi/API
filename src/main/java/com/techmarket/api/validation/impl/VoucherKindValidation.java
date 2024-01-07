package com.techmarket.api.validation.impl;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.validation.VoucherKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class VoucherKindValidation implements ConstraintValidator<VoucherKind,Integer> {
    private boolean allowNull;
    @Override
    public void initialize(VoucherKind constraintAnnotation) {
       allowNull= constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer kindVoucher, ConstraintValidatorContext constraintValidatorContext) {
        if(kindVoucher == null && allowNull) {
            return false;
        }
        if(!Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_ALL) &&
                !Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_GOLD_MEMBERSHIP)
                &&  !Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_SILVER_MEMBERSHIP)
                &&  !Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_DIAMOND_MEMEBERSHIP)
                &&  !Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_VIP_MEMEBERSHIP)
                &&  !Objects.equals(kindVoucher, UserBaseConstant.VOUCHER_KIND_NEW_MEMBERSHIP)) {
            return false;
        }
        return true;
    }
}
