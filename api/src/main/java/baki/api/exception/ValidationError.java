package baki.api.exception;

import javax.validation.Path;

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
 // OBAVEZNO je da Bindingresult bude ODMAH IZA polja sa anotacijom @Valid ako ga stavljamo
 // VAZNO !!!     BindingResult UVEK postoji 
 // Ako ga stavimo  - NE BACA SE EXCEPTION greske trazimo u njemu (ako hocemo da ih hendlamo )
 // Ako ga nestavimo - BACA SE EXCEPTION greske opet imamo wrapovane u njemu (odatle ih vadimo kad hendlamo exception )
 // U oba slucaja imamo objekat koji ce sadrzati sve greske konkretne validacije i od toga pravimo listu ValidationErrora
 // Odlican primer je signupdto gde moze biti mnogo gresaka (kako na nekom fieldu , tako i na celom object) 

 // @Validate - sa ovom anotacijom se validiraju path variable i request parameters koji stizu sa klijenta
 // Anotacija se stavlja na celu klasu ( npr kontroler ) , a same anotacije idu pre path variable ili request parametra
 // Posto su isti prosti (String ili int) nema slozenih anotacija 
 // Kad ovakva validacija NE USPE baca se ConstraintViolationException sa http 500 internal server error
 // ovo hendlamo u restexceptionhandleru (prebacimo na 400 bad request - jer je to u stvari ) + opis greske
 // Ovde uvek ide exception pa ga moramo hendlati ( nema binding resulta )
 // objekt greske ConstraitViolation sadrzi sve potrebno da napravimo ValidationError sa opisima greske

 /* VAZNO !!!         Ako zelimo OBE VALIDACIJE ISTOVREMENO
 (@Validate na klasu kontrolera + @Valid na requestbody metoda)onda ima vise opcija (koje sam isprobao) :
 ako STAVIM BindingResult iza @Valid    =>  NECE SE NIKAD BACITI MethodArgumentNotValidException 
    Greska se SVEJEDNO NIKAD NECE hendlati iz if(bindingresult.haserror) jel @Validate hendla obe tj. ConstraintViolationException ce izhendlati i MethodArgumentNotValidException
    Ovo radi posto sam napisao hendling , ali nije tako lep kao kad svaki exception zasebno hendla svoje greske 
 ZATO BOLJE :   
 da NESTAVIM BindingResult  =>  moguce su OBE greske , ali NE ISTOVREMENO tj. uvek se PRVO validira body, a POSLE path variable i/ili request parametri. Ovde je prednost sto se OBE greske hendlaju SVAKA na SVOJ nacin, a mana je sto se NEHENDLAJU istovremeno ( nego prvo body , pa druga ).
 NAPOMENA : Odlucio sam se za ovu drugu varijantu (prakticno samo kod PUT moze requestbody+pathvariable ili kod GET moze pathvariable+requestparam). TEORETSKI (probano , ali neznam use case validira sve zivo kako treba requestbody+pathvariable+2xrequestparam) 
 @Validate (ovo na klasi kontrolera)
 @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    @PutMapping("/{id}") 
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerInputDto customerInputDto,
                                            @PathVariable ("id") @Positive Long id,
                                            @RequestParam ("page") @Positive Long page,
                                            @RequestParam ("sort") @Pattern(regexp = "ASC|DSC" ) String sort) {                                          
        return customerService.updateCustomer(customerInputDto, id);    // zove samo ako su sva 4 validna
    }

 */ 
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

  // @Validate anotacija + idu constraint violation greske
  public ValidationError(Path propertyPath, Object object, String message) {
    this.object=propertyPath.toString();
    this.rejectedValue=object.toString();
    this.message=message;
}

    
}