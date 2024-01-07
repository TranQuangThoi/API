package com.techmarket.api.validation;

import com.techmarket.api.validation.impl.VoucherKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VoucherKindValidation.class)
@Documented
public @interface VoucherKind {
    boolean allowNull() default false;
    String message() default  "voucher invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
