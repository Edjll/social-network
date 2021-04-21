package ru.edjll.backend.validation.unique;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private final JdbcTemplate jdbcTemplate;
    private String column;
    private String table;

    public UniqueValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        column = constraintAnnotation.column();
        table = constraintAnnotation.table();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext ctx) {
        if (object == null) return true;
        return !jdbcTemplate.queryForObject(
                "select exists (select id from " + table + " where " + column + " = '" + object + "' limit 1)",
                Boolean.class
        );
    }
}