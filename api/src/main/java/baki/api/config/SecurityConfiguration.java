package baki.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import baki.api.service.jwtservice.JwtFilter;
import lombok.RequiredArgsConstructor;

/**
 * WebConfiguration
 */

@Configuration 
@RequiredArgsConstructor
@EnableWebSecurity
// donja anotacija za metod security kojim je puno razumljiviji i laksi za upotrebu od gomile antMatchersa
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()
        // VAZNO - posto koristim metod security moram sve da propustim prvo
        // RAZLOG - web/http security filter ide pre metod security filtera , pa ako nepropustim sve 
        // ili bar ono sto treba da je svima dostupno kao auth necemo nikad ni doci do metode
        // zato OVAKO sve propustiti , a onda u kontrolerima autorizovati SVAKI endpoint koji netreba svima da bude dostupan
        .antMatchers("/api/**").permitAll()
        // ovo sve netreba ako koristim metod level security kako sam opisao gore
        // .antMatchers("/api/admin/hello").hasRole("ADMIN")
        // .antMatchers("/api/user/hello").hasAnyRole("USER","ADMIN")
        // .antMatchers("/api/employee/hello").hasAnyRole("EMPLOYEE","ADMIN")
        // .antMatchers("/api/manager/hello").hasAnyRole("MANAGER","ADMIN")
        // .antMatchers(HttpMethod.POST , "/api/product").hasAuthority("PRODUCT_WRITE")
        // .antMatchers(HttpMethod.GET , "/api/product").hasAuthority("PRODUCT_READ")
        // .antMatchers(HttpMethod.POST , "/api/order").hasAuthority("ORDER_WRITE")
        // .antMatchers(HttpMethod.GET , "/api/order").hasAuthority("ORDER_READ")
        //.antMatchers("/api/auth/**" , "/api/home/**").permitAll()
        .anyRequest().authenticated()
       .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

    
}