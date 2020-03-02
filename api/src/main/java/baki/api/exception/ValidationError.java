package baki.api.exception;

import lombok.Data;

/**
 * ValidationError
 */

 // Zajednicki model - bean za greske validacije - obe vrste
 // VAZNO !
 // @Valid - sa ovom anotacijom se validira bean-object koji stize sa klijenta u requestbody ( obicno neki ulazni dto )
 // Anotacija se stavlja neposredno pre objekta koji se validira.
 // Kad validacija NE USPE baca se MethodArgumentNotValidException sa http 400 bad request
 // Drugi nacin je da validaciju prati BindingResult - onda se nece baciti exception nego ce greska biti u bindingresultu
 // U oba slucaja imamo objekat koji ce sadrzati sve greske konkretne validacije i od toga pravimo listu ValidationErrora
 // Odlican primer je signupdto gde moze biti mnogo gresaka (kako na nekom fieldu , tako i na celom object) 

 // @Validate - sa ovom anotacijom se validiraju path variable i request parameters koji stizu sa klijenta
 // Anotacija se stavlja na celu klasu ( npr kontroler ) , a same anotacije idu pre path variable ili request parametra
 // Posto su isti prosti (String ili int) nema slozenih anotacija 
 // Kad ovakva validacija NE USPE baca se ConstraintViolationException sa http 500 internal server error
 // Ovde uvek ide exception pa ga moramo hendlati ( nema binding resulta )
 // objekt greske ConstraitViolation sadrzi sve potrebno da napravimo ValidationError
@Data
 public class ValidationError {

   private String object;
   private String field;
   private Object rejectedValue;
   private String message;

   // @Valid anotacija + za field greske idu sva cetiri parametra
   public ValidationError(String object, String field, Object rejectedValue, String message) {
     this.object = object;
     this.field = field;
     this.rejectedValue = rejectedValue;
     this.message = message;
   }


   // @Valid anotacija + za global greske idu samo object i message
   public ValidationError(String object, String message) {
     this.object=object;
     this.message=message;
   }

   // @Validate anotacija + idu constraint violation greske to napravi kad se javi - zasad ne 
}