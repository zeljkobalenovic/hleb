package baki.api.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import baki.api.validator.customvalidator.RoleNamePresentValidator;

/**
 * RoleName  
 * primer custom validacije bean-a
 * validacija samo jedog fielda - anotacija se stavlja na taj field
 * validatedby navodimo konkretan class koji radi validaciju
 * message navodimo poruku ako validacija pukne
 * SVE ostalo je sablon - TAKO SE PISU ANOTACIJE za validaciju jednog FIELDA
 */

// opciono , ali se uvek stavlja zbog generisanja dokumentacije
@Documented
// OBAVEZNO - glavna stvar navodi se klasa koja implementira tu anotaciju
@Constraint(validatedBy = RoleNamePresentValidator.class)
// OBAVEZNO - navodi gde se sve moze staviti anotacija (ovde je na field beana)
@Target({ElementType.FIELD})  
// OBAVEZNO - uglavnom ide runtime
@Retention(RetentionPolicy.RUNTIME)
 public @interface RoleNamePresent {
    // default poruka - moze se overajdovati sa message u samom pozivu anotacije
    String message() default "Ne postoji ni JEDNA validna rola u trazenom setu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};    
}


// ova validacija proverava dali je posiljalac trazio bar jednu validnu rolu 