package ru.edjll.backend.validation.exists;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {

    String message() default "Not exist";

    String column();

    String table();

    Class<?> [] groups() default { };

    Class<? extends Payload> [] payload() default { };
}