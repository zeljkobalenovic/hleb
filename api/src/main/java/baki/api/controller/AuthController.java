package baki.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baki.api.dto.auth.SignInReqDto;
import baki.api.dto.auth.SignUpReqDto;
import baki.api.exception.ApiError;
import baki.api.service.AuthService;
import lombok.RequiredArgsConstructor;

/**
 * AuthController
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    

    @PostMapping("/signup")
    public ResponseEntity<?> signUp (@RequestBody @Valid SignUpReqDto signupuser , BindingResult bindingResult) {
       // VAZNO - prvi nacin hendlanja greske validacije - bez exceptiona sa binding result
       // ovako proverimo rezultat validacije pa obavestimo klijenta o greskama ako nije validno
       // moglo je i bez toga , ali onda u exception handleru hvatamo MethodArgumentNotValidException
       // i od njega pravimo obavestenje za klijenta

       /*  ovo je bila proba hvatanja exceptiona, dok nije bilo hendlanja za null pointer hendlo je throwable
       ,   a posle definisanja null pointer hendlanja koristio je taj hendling
       String s = null;
       int i = s.length(); 
       */
        if (bindingResult.hasErrors()) {      
            ApiError apiError = new ApiError(bindingResult);
            HttpStatus status = apiError.getStatus();             
              return new ResponseEntity<>(apiError,status);
        }        
       return authService.signup(signupuser);
    }

       // VAZNO - drugi nacin hendlanja greske validacije - sa exceptiona i sa binding result
       // Sada je OBAVEZNO u restexceptionhandleru hendlati MethodArgumentNotValidException
       // Obavestenje za klijenta sada pravimo od objekta greske ( koji je isti kao i u binding resultu u prvom primeru)
       // Binding result je ustvari samo wraper oko greske sa dodatkom jos nekih funkcionalnosti    

    @PostMapping("/signin") 
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInReqDto signinuser ){
            return authService.signin(signinuser);

    }

    
}