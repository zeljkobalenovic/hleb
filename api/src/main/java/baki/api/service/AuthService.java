package baki.api.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import baki.api.dto.SignInReqDto;
import baki.api.dto.SignInResDto;
import baki.api.dto.SignUpReqDto;
import baki.api.exception.ApiError;
import baki.api.model.Role;
import baki.api.model.RoleName;
import baki.api.model.User;
import baki.api.repository.RoleRepository;
import baki.api.repository.UserRepository;
import baki.api.service.jwtservice.JwtProperties;
//import baki.api.service.jwtservice.JwtUtil;
import baki.api.service.securityservice.MyUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * AuthService
 */

// Biznis logika ide u service

// ova lombok anotacija pravi konstruktor sa final poljima pa je kod lepsi
// best praksa je da se za di koristi konstruktor ( postoje i field i seter )
// cak nemora ni autowire jer ga spring boot implicitno koristi u ovom slucaju
@RequiredArgsConstructor
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
//	private final JwtUtil jwtUtil;

	@Transactional
	public ResponseEntity<?> signup(SignUpReqDto signupuser) {
		// ulazni parametar je validan LOGICKI - to se proverava u kontroleru pre poziva
		// servis metode

		/*
		 * u servisu radimo biznis validaciju ( da li klijentov request moze biti
		 * ispunjen ) , ako ne obavestavamo klijenta imamo 3 provere ( username i email
		 * ako POSTOJE bacamo exception i vracamo ApiError klijentu) + trazene role ako
		 * NE POSTOJE bacamo exception i vracamo ApiError klijentu + ovo cu specificno
		 * hendlati ako BAR JEDNA rola od trazenih moze da se dodeli upisacu je , a one
		 * koje nemogu zanemariti ako NI JEDNA rola ne moze bacicu exception u svim
		 * problematicnim slucajevima ce http biti bad request ove provere mogu samo
		 * upitima u bazu VAZNO ! koristi se transactional anotacija jer posle svih
		 * provera ide upis novog korisnika. U slucaju multiuser bez te anotacije moglo
		 * bi se desiti da mi proverimo nesto (npr username) , a da drugi korisnik unese
		 * bas to ime izmedju provere i samog upisa sto bi dovelo do db greske naravno
		 * da se podrazumeva da ako se baza externo pravi - ne iz hibernate koda vaze
		 * isti constrainti NAPOMENA : Ako korisnik trazi rolu USER odmah pri upisu
		 * korisnik ce biti aktivan ako trazi neke druge role pri upisu ce biti
		 * neaktivan ( to mora da odobri admionistrator naknadno )
		 */

		if (userRepository.existsByUsername(signupuser.getUsername())) {
			String message = "Korisnik : " + signupuser.getUsername() + " vec postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		if (userRepository.existsByEmail(signupuser.getEmail())) {
			String message = "Email : " + signupuser.getEmail() + " vec postoji.";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}

		// da nebi DVA puta radili - ako nije empty - onda je odmah dobar za mapiranje dto -> dao
		Set<Role> checkedSet = checkRoles(signupuser.getRoles());

		if (checkedSet.isEmpty()) {
			String message = "Niste trazili NI JEDNU dozvoljenu rolu";
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}

		// ako je sve proslo ok onda mapiranje dto->dao
		User user = mapDtoSignUpToUser(signupuser, checkedSet);
		// i cuvanje u bazi		
		return new ResponseEntity<>(userRepository.save(user),HttpStatus.CREATED);

	}

	// Pomocne metode
		
		// Ova metoda kontrolise trazene role i u istom prolazu pravi set rola za maping dto->dao
		// PAZNJA ! kod logicke kontrole smo kontrolisali da li je BAR JEDAN string iz seta enum rolaname
		// Ova metoda kontrolise i da li je enum i da li je ta rola u bazi rola (npr GENERALMANAGER nije)
		// zato prodje logicku , a ne ovu kontrolu ( npr. GENERALMANAGER je predvidjeno za buducu upotrebu )
	private Set<Role> checkRoles(Set<String> roles) {
		// prvo napravimo prazan set
		Set<Role> sr = new HashSet<>();
		// izvucemo sve dozvoljene role iz baze		
		roleRepository.findAll().stream()
					  // kroz filter pustimo samo one koje je klijent trazio
					  .filter( rola -> roles.contains(rola.getName().toString() ))
					  // ako neka prodje ubacujemo je u set
					  .forEach(rola -> sr.add(rola));
			return sr;
		}

	// maping je prost jer je sve vec pripremljeno	
	private User mapDtoSignUpToUser(SignUpReqDto signupuser, Set<Role> checkedSet) {
			User user = new User();
			user.setUsername(signupuser.getUsername());
			user.setEmail(signupuser.getEmail());
			user.setPassword(passwordEncoder.encode(signupuser.getPassword()));
			user.setRoles(checkedSet);
			user.setActive(checkedSet.size()==1 && checkedSet.stream().anyMatch(role->role.getName().equals(RoleName.USER)));
			return user;
			
		}
	
	

	public ResponseEntity<?> signin(SignInReqDto signinuser) {
		// ulazni parametar je validan LOGICKI - to se proverava u kontroleru pre poziva servis metode

		// 1 pravimo token za autentikaciju od ulaznog podatka - default spring security token za username-password 
		UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(signinuser.getUsername()
																							  , signinuser.getPassword());
	
		// 2 pokusavamo autentikaciju sa tim tokenom		
		try {
			Authentication auth = authenticationManager.authenticate(authtoken)	;
			
			
			// ako username ili password nisu ok bacice bad credential
			// ako user nije aktiviran bacice disabled exception
			// INTERESANTNO - prvo proverava aktiv ( ako je username ok - nadje ga u bazi pa prvo proveri activ 
			// pa tek onda password - tj. bacice da user nije aktivan i ako password ne valja )
			// ako se username promasi svakako baca bad credentials
			// mogao sam hendlati i user not find u tom slucaju koji baca userdetailsservice , ali nisam jer
			// je praksa da se zbog potencijalnog hakovanja ne objasnjava sta ne valja  nego zveknemo bad credentials
			// oba smo mogli hendlati u exceptioncontroleru ili ovako - probao sam oba 

			// VAZNO - Zbog tipa autentikacije i autorizacije NE SME se sacuvati auth objekat u security contextu
			// obicno se cuva i onda klijent ne mora stalno da ponavlja username+password kod svakog requesta
			// SecurityContextHolder.getContext().setAuthentication(auth); - ovo OBAVEZNO iskljuciti !!!
			// Koristicu potpuno nezavisno hendlanje svakog pojedinacnog requesta (STATELES) tj zabranicu springu
			// da pravi bilo kaklve sesije nego ce se svaki put sve raditi iz jwt tokena koji klijent posalje

			// ako je stigao dovde uspesno je prosla autentikacija pa formiram response klijentu
			// response ce biti objekat tipa AuthResponse koji ce imati username, authority i jwt token
			// sva tri se prave na osnovu userdetails koji se nalazi u auth objektu
			// ovde ide obicno  userdetails , ali posto sam ga prosirio sa email moram moju klasu jer hocu email u jwt tokenu
			// ovo dole je provereno auth principal je myuserdetails klasa , a ona se moze kastovati u userdetails
			// jer je implementira , ali onda se gube dodatna polja-metode , kod mene email 
			// zato ne koristim interfejs userdetail nego njegovu implementaciju myuserdetails
			// System.out.println(auth.getPrincipal().getClass().getName());

			MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
			
			String jwt = 
			Jwts.builder()
				.setSubject(myUserDetails.getUsername())
				.claim("authorities", myUserDetails.getAuthorities())
				.claim("email", myUserDetails.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.signWith(Keys.hmacShaKeyFor(JwtProperties.SECRET.getBytes()))
				.compact();
			
				SignInResDto response = new SignInResDto(jwt);
				return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

		} catch (BadCredentialsException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Bad credentials : User and/or password not valid", e);
			HttpStatus status = apiError.getStatus();
			return new ResponseEntity<>(apiError, status);
		} catch (DisabledException e) {
			ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "User not activated. Contact admin to activate acount", e);
			HttpStatus status = apiError.getStatus();
			return new ResponseEntity<>(apiError, status);
		} 
																						  
		
		
	}

    
}