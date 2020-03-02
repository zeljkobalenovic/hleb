package baki.api.service.securityservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import baki.api.model.Role;
import baki.api.model.User;

// UserDetails je spring security predstavljanje korisnika , dok je moj u bazi predstavljen sa objektom user
// zato OBAVEZNO pravimo od usera u bazi spring security usera (userdetails) tako sto mapiramo user->userdetails
// i implementiramo userdetails interfejs cime smo spramni da moj user iz baze bude koriscen u spring security

// interfejs obuhvata 4 get metode koje moja implementacija mora da odradi 

// stvarno cu koristiti 4 dok ce preostale 3 samo vracati true jer mi ne trebaju

public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1234461780106166342L;
    // obaveznih 7 su :
    // 4 koje cu koristiti
    private String username;
    private String password;
    private boolean isEnabled;
    private Collection<? extends GrantedAuthority> authorities;    
    /*
     * 3 koje necu koristiti 
     * private boolean isAccountNonExpired; 
     * private boolean isAccountNonLocked;
     * private boolean isCredentialsNonExpired;
     */
    // dodacu 1 koji nije obavezan u userdetails , ali ga imam u user pa da se i to demonstrira
    private String email;

    // VAZNO - konstruktor za pravljenje spring security userdetails od mog usera iz baze (pravim 4 obavezna + 1 dodatni)
    MyUserDetails(User user) {
        // 4 obavezna
        // VAZNO username userdetailsa NE MORA biti username usera , moglo je biti npr.email (onda login sa email i password)
        this.username = user.getUsername();
        // VAZNO password mora biti kriptovan UVEK
        this.password = user.getPassword();
        // ovo je isto u oba samo se drugacije zove
        this.isEnabled = user.isActive();
        // VAZNO - AUTHORITIES user detailsa obuhvata i role i permissione !!! zato obratiti paznju na ispravno mapiranje
        // ako se koriste samo role bez permissiona - odkomentarisi sta koristis
        // this.authorities=addRole(user.getRoles());      
        // ako se koriste i role i permissioni sto je moj slucaj
        this.authorities = addRoleAndPermission(user.getRoles());
        // ovo je dodatni neobavezni u userdetails 
        this.email=user.getEmail();
    }    

    // pomocni metodi koje koristi konstruktor myuserdetails za authorities koji jednako tretira i role i permissione 
    /* ovaj ne koristi
    
    private Set<SimpleGrantedAuthority> addRole(Set<Role> roles) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
        });
        return authorities;
    } 
    */ 
    // ovaj koristim
    private Set<SimpleGrantedAuthority> addRoleAndPermission(Set<Role> roles) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName().name()));
            });
        });
        return authorities;
    }

    // OBAVEZNE metode interfejsa koji implementiram

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    // plus dodatni 
    public String getEmail() {
        return email;
    }



}
