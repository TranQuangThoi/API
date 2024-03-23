package com.techmarket.api.validation;

import com.techmarket.api.validation.impl.RefundsStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RefundsStateValidation.class)
@Documented
public @interface RefundsState {
    boolean allowNull() default false;
    String message() default  "state refunds invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
