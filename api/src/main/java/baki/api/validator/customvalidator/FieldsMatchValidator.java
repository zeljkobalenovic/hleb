package baki.api.validator.customvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import baki.api.validator.annotation.FieldsMatch;

/**
 * FieldsMatchValidator
 */

 // OBAVEZNO - implementacija - prvi parametar je anotacija , a drugi object ( zato sto moze da uporedjuje bilo sta)
public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    // parametri sta se validira (u njih ce biti prosledjeni nazivi dva polja koja proveravamo)
    String field ;
    String fieldMatch;

    // posto ova validacija ima parametre tu se inicijalizuju parametrima iz samog poziva
    // posto je poziv lista prvo se proslede password i confirmpassword , pa se uradi validacija
    // pa onda email i confirmemail pa se uradi validacija , itd po celoj listi
    // VAZNO - u field i fieldmatch se upisuju NAZIVI polja beana koji se uporedjuju 
    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
       }

    
       // VAZNO - logika validacije - validira se ceo bean zato je object       
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // nacin da se iz objecta - beana izvuku dva objecta(kod mene stringa, ali ne mora-moze bilo sta zato object)
        
        // objekt1 = iz celog beana - objecta ( value = sto je ceo objekt signupdto ) izvuci value polja beana koje je u 
        // field ( a tu je password ili email) - ovo je konstrukcija za to
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        // objekt2 = iz celog beana - objecta ( value = sto je ceo objekt signupdto ) izvuci value polja beana koje je u 
        // fieldmatch ( a tu je confirmpassword ili confirmemail) - ovo je konstrukcija za to
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        // klasicno uporedjivanje dali su dva objekta ista
        if ( fieldValue !=null) { return fieldValue.equals(fieldMatchValue);}
        else return fieldMatchValue == null;

    }

    

    
}