package ru.edjll.backend.validation.exists;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.edjll.backend.util.BeanUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsValidator implements ConstraintValidator<Exists, Object> {

    private JpaRepository<?, Object> repository;

    @Override
    public void initialize(Exists constraintAnnotation) {
        repository = (JpaRepository<?, Object>) BeanUtil.getBean(constraintAnnotation.typeRepository());
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext ctx) {
        if (object == null) return true;
        return repository.existsById(object);
    }
}