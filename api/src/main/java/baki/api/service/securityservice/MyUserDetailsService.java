package baki.api.service.securityservice;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import baki.api.model.User;
import baki.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * MyUserDetailsService
 */

 @RequiredArgsConstructor
 @Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // trazimo usera po username - ako nadje vraca usera iz baze , ako ne null 
            Optional<User> user = userRepository.findByUsername(username);
            // ako ga ne nadje 
            user.orElseThrow(()->new UsernameNotFoundException("User not found with username : "+username));
            // ako ga nadje radi mapiranje ( koristi se konstruktor MyUserDetails(user)). Na kraju get jer je optional
            // tj. od optional usera dobijamo optional myuserdetails
            // da nema usera bacio bi exception NoSuchElementException , ali ako stigne dovde uvek ga ima sto smo
            // proverili u prethodnom redu - baca propisani exception ako ne nadje usera
            return user.map(MyUserDetails::new).get();
    }

    
}