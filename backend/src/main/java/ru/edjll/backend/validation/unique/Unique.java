package ru.edjll.backend.validation.unique;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "Already exist";

    String column();

    String table();

    Class<?> [] groups() default { };

    Class<? extends Payload> [] payload() default { };
}