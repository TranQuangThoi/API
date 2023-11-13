package com.techmarket.api.validation;

import com.techmarket.api.validation.impl.PaymentKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentKindValidation.class)
@Documented
public @interface PaymentKind {
    boolean allowNull() default false;
    String message() default  "payment Kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
