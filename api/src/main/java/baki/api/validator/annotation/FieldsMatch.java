package baki.api.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import baki.api.validator.customvalidator.FieldsMatchValidator;

/**
 * FieldsMatch
 * primer custom validacije bean-a
 * validacija celog bean-a uporedjuje se vise polja i slicno - anotacija se stavlja na bean - classu
 * validatedby navodimo konkretan class koji radi validaciju
 * message navodimo poruku ako validacija pukne
 * SVE ostalo je sablon - TAKO SE PISU ANOTACIJE za validaciju celog Bean-a 
 */

 @Documented
 // VAZNO - kad validacija ide na ceo bean ( klasu-objekat ) onda ide type 
 @Target(ElementType.TYPE)   
 @Retention(RetentionPolicy.RUNTIME)
 @Constraint(validatedBy = FieldsMatchValidator.class)
public @interface FieldsMatch {

    String message() default "Fields values don't match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{}; 

   // ova validacija uporedjuje bilo koja dva fielda u beanu (objecta jer mogu biti bilo kakvog tipa - ne mora string )
   // zato ima dva parametra koje ovde navodimo ( pri samom pozivanju anotacije navodimo te parametre - vidi u dto kako)
   // message je kao i kod prostije varijante parametar koji je opcioni i pri pozivu anotacije overajdujemo default
    String field();
    String fieldMatch();
    // string je jer je naziv parametra - bice prosledjeni nazivi fieldova za usporedbu
    // sami fieldovi mogu biti BILO KOJEG TIPA - zato se u validatoru stavlja object ( mi uporedjujemo stvarno stringove)
    // ali ova anotacija prakticno uporedjuje match bilo koja dva objekta

    // ovo je sablon ako hocemo vise parova da validiramo ( kod mene password i email - da je jedan ovo je nepotrebno)
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsMatch[] value();
    }    
}