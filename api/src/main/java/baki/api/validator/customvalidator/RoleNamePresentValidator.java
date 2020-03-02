package baki.api.validator.customvalidator;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import baki.api.model.RoleName;
import baki.api.validator.annotation.RoleNamePresent;

/**
 * RoleNameValidator
 */

 // OBAVEZNO - implementira navedeno ( prvi parametar je pripadajuca anotacija , a drugi sta validira)
 //            u mom slucaju validira se set<string> tog tipa je i prvi parametar isvalid metode 
public class RoleNamePresentValidator implements ConstraintValidator<RoleNamePresent, Set<String>> {

    // NIJE OBAVEZNO - sluzi da inicijalizuje parametre navedene u anotaciji 
    @Override
    public void initialize(RoleNamePresent constraintAnnotation) {
    }
    
    // OBAVEZNO - ovo se mora overajdovati - biznis logika validacije
    @Override
    public boolean isValid(Set<String> rolesField, ConstraintValidatorContext context) {
        if (rolesField==null) {return false;}
        Set<RoleName> setrn = new HashSet<>();
        rolesField.forEach(role -> {
            try {
                RoleName rn = RoleName.valueOf(role);
                setrn.add(rn);
            } catch (Exception e) {
              //  System.out.println(role + "  nije validna dozvoljena rola");
            }
        });

        if (setrn.isEmpty()) {
            return false;
        }
        return true;
    }

    

    
}