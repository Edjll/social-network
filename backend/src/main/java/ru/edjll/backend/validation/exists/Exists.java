package ru.edjll.backend.validation.exists;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {

    String message() default "Not exist";

    Class<? extends JpaRepository<?, ?>> typeRepository();

    Class<?> [] groups() default { };

    Class<? extends Payload> [] payload() default { };
}