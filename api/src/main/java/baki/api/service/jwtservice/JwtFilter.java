package baki.api.service.jwtservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.var;

/**
 * JwtFilter
 */

// ovaj filter ide pre usernamepasswordauthenticationfiltera - presrece request
// , trazi jwt token u authorization
// headeru , validira ga ( expiration i sl. ) , izvuce username iz jwt tokena i
// na osnovu istog napravi
// novi usernamepasswordauthenticationtoken za dalje procesiranje
// obavezno extend onceperrequestfiltera i overajdovati dofilterinternal i posle
// baciti nazad u lanac gde springsecurity
// izvrsava preostale poslove

@Component
public class JwtFilter extends OncePerRequestFilter {

     

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // prvo pokupimo token iz hedera
        String authorizationHeader = request.getHeader("Authorization"); 

        // ako ga nema ili je prazan string ili nije Bearer propustimo request dalje bez da ista radimo
        // Time on ide na UsernamePasswordAuthenticationFilter koji ce ubiti request jer nece moci da ga autuntikuje
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Ako ga ima i pocinje sa Bearer odsecamo prvih 7 karaktera da bi dosli do cistog jwt tokena
        String jwtToken = authorizationHeader.substring(7);

        // Sada izvlacimo sta nam treba iz jwt tokena
        // VAZNO sve ide u try/catch zato sto jwt moze biti neispravan i/ili da je istekao

        // VAZNO  koristio sam drugu biblioteku za jwt , i drugaciji nacin rada kad dodjem do usera iz jwt
        // u repo springsecurity ima druga biblioteka za jwt i vadjenje svega iz baze kad se dodje do jwt
        // OVDE - kad dodjemo iz jwt do username NE IDEM u bazu po ostalo nego SVE (role i permissione) takodje vadim iz jwt
        // pa iz toga pravim token za autentikaciju koji ubacujem u security context
        // moguca su oba nacina ( s obzirom da jwt traje 1 sat opredelio sam se za ovaj - bez naknadne provere u bazi)

        try {
            // za autentikaciju/autorizaciju treba nam username iz jwt tokena (jedinstvena identifikacija korisnika)
            // posto su metode deprecated ova anotacija
            @SuppressWarnings("deprecation")
            Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(JwtProperties.SECRET.getBytes()))
                .parseClaimsJws(jwtToken);

                Claims body = claimsJws.getBody();
                String username = body.getSubject();

                // VAZNO - ovde mozemo u bazu po authoritije kao u repo spring security ili odmah vaditi authoritije iz claima
                // koji saljemo i klijentu u okviru jwt , pa sam se za to opredelio
                // ova anotacija objekt iz claima vadimo treba ovako
                @SuppressWarnings("unchecked")
                var authorities = (List<Map<String,String>>) body.get("authorities");

                Set<SimpleGrantedAuthority> simpleGrantedAuthority = authorities.stream()
                                            .map(m->new SimpleGrantedAuthority(m.get("authority")))
                                            .collect(Collectors.toSet());

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthority);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ako jwt ne valja ( npr istekao je ili je neko prckao po njemu ide exception dole)
	

        } catch (JwtException e) {
         
            // VAZNO - ako nesto nevalja sa jwt tokenom ovde zavrsava i baca access denied zato sto je 403
            // Kako da se hendla odavde prava greska koja je jwtexception ???
            // exception handler nehendla jer greska nije u kontroleru nego u filteru koji je deo spring security
            // zato se vraca 403 forbiden i hendla klijentu kao access denied 
        }

    filterChain.doFilter(request, response);
        
    }

    
}