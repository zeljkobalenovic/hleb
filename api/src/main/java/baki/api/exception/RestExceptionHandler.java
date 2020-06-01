package baki.api.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



/**
 * RestExceptionHandler
 */

// ova anotacija kazuje springu da je kontroler specijalnog tipa (3 moguca od
// kojih je jedan exceptionhandler)
// takodje i centralizuje exception handling da vazi za SVE Controllere u mom
// REST Apiju
// extenduje se default springov responseentity za greske sa onim koje cu ja
// pisati
// moze se overajdovati springov exseption handling na neke greske
// ili se moze dodati exception handling za greske koje se javljaju ( a vec
// postoje )
// ili se moze napisati exception handling za greske koje ne postoje ( onda se
// prvo mora definisati taj cusstom exception)
// takodje moze jeadan exception handling da bude za proizvoljan broj
// razlicitih exceptiona
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // najopstiji slucaj - bilo koju gresku ce izhendlati ovako
    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleAnyException(Throwable e, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, null, e);
        HttpStatus status = apiError.getStatus();
        return new ResponseEntity<>(apiError, status);
    }

    // hendlanje @Valid kad neide preko binding resulta
    // kad ide preko binding resulta ne hendla se uopste jer se ne baca exception
    // Posto se po defaultu ova greska vec hendla overajdujemo + ne ide anotacija @exception handler   
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                        HttpHeaders headers, HttpStatus status1, WebRequest request) {
        ApiError apiError = new ApiError(e);
        HttpStatus status = apiError.getStatus();
        return new ResponseEntity<>(apiError, status);        
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        ApiError apiError = new ApiError(e);
        HttpStatus status = apiError.getStatus();
        return new ResponseEntity<>(apiError, status);                                      
    }

    // Primer hendlanja nulpointer greske - posto je specificnija od throwable kad se greska desi hendlace je ovako
    // da ne napisemo ovaj hendling koristio bi se throwable jer obuhvata i ovu gresku
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException e, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Doslo je do null pointer greske", e);
        HttpStatus status = apiError.getStatus();
        return new ResponseEntity<>(apiError, status);
    }

    // hendlanje greske koju baca @PreAuthorize ili @PostAuthorize kad korisnik nema rolu ili permission za metod
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Zabranjeno - Nemate ovlascenje - obratite se administratoru", e);
        HttpStatus status = apiError.getStatus();
        return new ResponseEntity<>(apiError, status);
    }
    
    
    
}