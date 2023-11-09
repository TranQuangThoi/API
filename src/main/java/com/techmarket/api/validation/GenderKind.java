package com.techmarket.api.validation;

import com.techmarket.api.validation.impl.GenderKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderKindValidation.class)
@Documented
public @interface GenderKind {
    boolean allowNull() default false;
    String message() default "gender kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
