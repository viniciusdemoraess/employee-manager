package com.seplag.employee_manager.util;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FotoValidaListaValidator.class})
public @interface FotoValida {

  public String message() default "Tipo de arquivo inválido";

  public Class<?>[] groups() default {};

  public Class<? extends Payload>[] payload() default {};
}
