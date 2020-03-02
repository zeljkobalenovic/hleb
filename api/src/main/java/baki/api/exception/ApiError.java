package baki.api.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import lombok.Data;




/**
 * ApiError
 */

 // kad bilo gde u apiju dodje do greske hocu ovaj model da bacim klijentu u bodyju response entytija
@Data
 public class ApiError {

    private Date timestamp;                  // kad je nastala grseka
    private HttpStatus status;               // status greske 400+ ili 500+
    private String message;                  // poruka razumljiva klijentu ( ovo se internacionalizuje po potrebi )
    private String debugmessage;             // poruka razumljiva programeru za trazenje uzroka ( sistemska greska )
    private List<ValidationError> subErrors; // ako se radi validacija moze biti proizvoljno gresaka


    // ovakav konstruktor za sve moguce uobicajene greske - koristice se u restexceptioncontroleru za 
    // pravljenje response entitija za slanj klijentu za razne greske
    public ApiError(HttpStatus status, String message, Throwable e) {
        this.timestamp = new Date(System.currentTimeMillis());
        this.status = status;
        if (message != null) {
            this.message = message;
        } else {
            this.message = "Unexpected error";
        }        
        this.debugmessage = e.toString();
    }

    // ovakav konstruktor za sve moguce biznis logika greske - nema pravog exceptiona
        public ApiError(HttpStatus status, String message) {
        this.timestamp = new Date();
        this.status = status;
        if (message != null) {
            this.message = message;
        } else {
            this.message = "Unexpected error";
        }        
   }

    // pomocni konstruktor za validation error sa binding result - primer signupdto

    public ApiError(BindingResult bindingResult) {
        this.timestamp = new Date();
        this.status = HttpStatus.BAD_REQUEST;
        this.message = "Ulazni podatci nisu validni. Broj gresaka : " + bindingResult.getErrorCount();
        // ovo je visak this.debugmessage = bindingResult.toString();
        this.subErrors = addValidationErrors(bindingResult);
        
    }

    // pomocni konstruktor za validation error bez binding result sa bacanjem exceptiona  primer signindto
    // binding result i dalje sadrzi objekat greske validacije , ali se greska baca 

    public ApiError(MethodArgumentNotValidException e) {
        this.timestamp = new Date();
        this.status = HttpStatus.BAD_REQUEST;
        this.message = "Ulazni podatci nisu validni. Broj gresaka : " + e.getBindingResult().getErrorCount();
        // ovo je visak this.debugmessage = e.getBindingResult().toString();
        this.subErrors = addValidationErrors(e.getBindingResult());
    }

    // pomocna metoda za validation error listu gresaka ( vazi za obe gornje )
    public List<ValidationError> addValidationErrors (BindingResult bindingResult) {
        List<ValidationError> lve = new ArrayList<>();
        if ( bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().forEach(err-> {
                ValidationError ve = new ValidationError(err.getObjectName(), 
                                                         err.getField(),
                                                         err.getRejectedValue(),
                                                         err.getDefaultMessage());
                lve.add(ve);
            });
        }    
        if ( bindingResult.hasGlobalErrors()) {
            bindingResult.getGlobalErrors().forEach(err-> {
                ValidationError ve = new ValidationError(err.getObjectName(), 
                                                         err.getDefaultMessage());
                lve.add(ve);
            });
        }    
        return lve;
    }     

    // VAZNO moguca je i treca vrsta kod @Validate anotacije sa bacanjem ConstraintViolationException exceptiona
    // plus http 500. Ovo treba ishendlati u rest exception handleru da vrati response sa apierror u body i 
    // http 400 bad request ( jer je to ustvari greska klijenta ) - videti sta treba ovde dopisati za takve greske
    
    

}




