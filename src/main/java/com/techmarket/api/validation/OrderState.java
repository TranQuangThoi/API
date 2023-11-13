package com.techmarket.api.validation;

import com.techmarket.api.validation.impl.OrderStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderStateValidation.class)
@Documented
public @interface OrderState {
    boolean allowNull() default false;
    String message() default  "state order invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
