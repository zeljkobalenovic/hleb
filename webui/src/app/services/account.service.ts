import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, toArray } from 'rxjs/operators'
// VAZNO ovaki se importuje biblioteka za dekodovanje jwt - prethodno instalisana sa npm i nista vise netreba
import * as jwt_decode from "jwt-decode";
import { Router } from '@angular/router';
import { ObservableStore } from '@codewithdan/observable-store';
import { StoreState } from '../shared/store/store-state';



// ova anotacija zajedno sa odgovarajucim importom kazuje angularu da je ova klasa servis i da je dostupna svima 
// (zbog providedin root). Stvar je ista kao u spring boot tj. servis se putem dependenci injectiona koristi u drugim klasama
// kao sto je u spring boot di best praksa konstruktor , tako se i u angularu korist konstruktor di
// klasa koja hoce da koristi ovaj servis navodi ga u svom konstruktoru
@Injectable({
  providedIn: 'root'
})

// namena ove klase je da obezbedi komponentama angulara (raznim) potrebne informacije i metode za rad sa api koji ima veze
// sa autentikacijom i autorizacijom ( znaci metode koje pristupaju auth delu api - signup i signin), promenjive koje cuvaju
// odredjena stanja i parametre autha i tome slicno
export class AccountService extends ObservableStore<StoreState>{

  // posto pristupa api obavezno mi treba http servis - di u konstruktor ( ovo je jedan od gotovih servisa angulara)
  constructor(private http:HttpClient , private router:Router) {
    super({});
  }

  // properties koji cuvaju value kojima ce pristupati korisnici servisa (komponente)
  // base url na serveru za api
  private baseUrl : string = "/api/auth/";
  // da li je korisnik logovan ili nije , ime logovanog korisnika , dodeljeni permissioni logovanog korisnika
  /*
  private loginStatus : boolean = fakse;
  private userName : string;
  private userAuthority : string [];
  */
  // VAZNO - oko gornjih promenjivih ( ako su private , onda komponenta koja koristi servis nemoze da im pristupi,
  // a ako su public onda ceo servis nema smisla jer npr. login komponenta moze da ih promeni kako ona hoce )

  // RESENJE - rxjs (asinhroni js - sa svojim tipovima promenjivih - observable,subject,behavior subject ...)
  // onda im komponenta koja koristi servis pristupa sa subsribe (prijavljuje se da prati vrednosti takvih promenjivih)
  /* Razlikuju se sto se vidi iz tabele pa upotrebljavamo sta nam treba
  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃         Observable                  ┃     BehaviorSubject/Subject         ┃      
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ 
┃ Is just a function, no state        ┃ Has state. Stores data in memory    ┃
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ Code run for each observer          ┃ Same code run                       ┃
┃                                     ┃ only once for all observers         ┃
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ Creates only Observable             ┃Can create and also listen Observable┃
┃ ( data producer alone )             ┃ ( data producer and consumer )      ┃
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
┃ Usage: Simple Observable with only  ┃ Usage:                              ┃
┃ one Obeserver.                      ┃ * Store data and modify frequently  ┃
┃                                     ┃ * Multiple observers listen to data ┃
┃                                     ┃ * Proxy between Observable  and     ┃
┃                                     ┃   Observer                          ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
*/
// Posto je observable unicast (isporucuje vrednost samo onom subscriberu koji je trazi i kada je trazi - NE)
// Subject je multicast tj. isporucuje - trigeruje vrednost svim subscriberima ( ali nema inicijalno stanje - NE)
// Nase promenjive treba da slusa vise komponenti , a takodje treba ih i inicijalizovati zato BehaviorSuject
// zato se promenjive prave sa new , a u zagradi je pocetna vrednost

private loginStatus = new BehaviorSubject<boolean>(this.checkLoginStatus());
private userName = new BehaviorSubject<string>(localStorage.getItem('username'));
private userAuthority = new BehaviorSubject<string>(localStorage.getItem('authorities'));

// pocetna vrednost uvek false - treba funkcija nemoze samo da se stavi false
checkLoginStatus() : boolean {
  if (localStorage.getItem('loginStatus') == '1') {
    return true;
  }
  return false;
}

// signin metod
signin(username:string,password:string) {
  // servisna metoda salje zahtev na server i prima rezultat (ako je ok dobice jwttoken u body , a ako je error opis grske)
  // metoda pipe nam omogucava da nad rezultatom uradimo neke funkcije ( kao stream u javi) i to proizvoljno mnogo funkcija 
  // jedna za drugom , gde svaka sledeca radi sa rezultatom prethodne 
  return this.http.post<any>(this.baseUrl+"signin",{username,password}).pipe(
    // prvo map - da uradi nesto sa rezultatom ( ako je eror nista , a ako je rezultat ok objasnjeno je dole)
    map(result => {
      // samo ako je rezultat ok (onda je u bodyju jwt token) i korisnik se uspesno logovao
      // 1 dekodovati ga ( izvuci srednji deo gde je json sa podatcima)
      // 2 ubaciti dekodovane podatke u local storage
      // result je json  !!! koji sadrzi jwt token sa servera
      if (result && result.jwt) {  
          // ovako menjamo vrednost behavior subjecta
          this.loginStatus.next(true);
          // upisujemo promenjivu u localstorage ( ako je u sesion storage onda samo dok je u prozoru pregledaca)
          // koristim local storage jer u njemu ostaje proizvoljno dugo - cak i kad se zatvori pregledac 
          // VAZNO - u storage idu samo parovi key-value ( MORAJU biti string-string) zato npr nemoze true dole
          localStorage.setItem('loginStatus','1'); 
          // upisujem i ceo jwttoken jer ga treba slati uz svaki request na api
          localStorage.setItem('jwt', result.jwt); 
          // dekodujem jwt token da izvadim srednji deo gde su korisni podatci (biblioteka uradi sve rezultat je stringify json)
          let decodedjwt = jwt_decode(result.jwt);
          // dalje iz dekodovanog vadim i smestam u storage username,expiration i permissione logovanog korisnika
          localStorage.setItem('username', decodedjwt['sub']);
          localStorage.setItem('expiration',decodedjwt['exp']);
          // permissioni su malo komplikovaniji pa posto moramo upisati kao string treba prvo dobiti taj string iz jsona 
          // u kome je smesten ( biram string koji ce sadrzati sve permissione razdvojene zarezom - to u storage )
          let authorities = decodedjwt['authorities'];
          // kad imam json sa permissionima ( server salje sve kao authority - i role i permissione) 
          // pravim string sa svim authoritijima razdvojenim zarezom i njega upisujem u storage
          let userauthority = '';
          authorities.forEach(element => {
              if (userauthority==='') {
                userauthority = userauthority.concat(element['authority'])
              } 
              else {
                userauthority = userauthority.concat(",")
                userauthority = userauthority.concat(element['authority'])
              }              
          });            
          localStorage.setItem('authorities',userauthority);
          // upisemo sve u behavior subjecte. VAZNO !!! ovim postizemo da subscriberi te tri promenjive budu obavesteni 
          // o promeni vrednosti , pa u skladu sa tim reaguju
          this.userName.next(localStorage.getItem('username'));
          this.userAuthority.next(localStorage.getItem('authorities'));
      }
      // valjao ili ne result prosledjujemo onom ko je pozvao metodu servisa ( u mom slucaju login komponenta )
      // VAZNO greske ce hendlati onaj ko je pozvao metodu servisa
      // s tim da ako je valjao imamo u local storage namesteno sve sto nam treba 
      return result;
    })
  );

}

// VAZNO - kod logina je servis trebao nesto da uradi (sacuva u local storage i slicno)
// kod registracije samo prosledjuje (ispitati kasnije gde se hendla greska jer smo ceo odgovor mogli poslati komponenti
// koja je zvala servisnu metodu , pa tamo hendlati result i error ovog observable sa subscribe )
signup(username:string,password:string,cpassword:string,email:string,cemail:string,roles:string[]){
  // nemamo potrebe nista da radimo sa odgovorom ovde , zato ga prosledjujemo kontroli koja ce obraditi odgovor
  // bio ispravan (vratice registrovanog usera ) ili neispravan ( vratice gresku sa servera )
  // na server se obicno nesalju confirm password i email , ali ja hocu i to da probam - serversku validaciju
  // za probu cu miskljuciti klijentsku validaciju 
  // VAZNO - obicno se i na klijent strani napravi dto objekat (koji bi sadrzao ovih 6 . ali zasad ovako )
  return this.http.post<any>(this.baseUrl+"signup",{username,password,cpassword,email,cemail,roles}).pipe(map(
    result=> {
      // ako je rezultat ok stize json sa novoregistrovanim korisnikom - prosledjujemo komponenti da to prikaze u modalu
      return result;
    }, error => {
      // ako je server poslao gresku takodje hocu da je komponenta prikaze u maodalu (jednostavniju i komplikovaniju gresku)
      return error;
    }
  ));
}

// signout - logout metoda
signout() {
  this.loginStatus.next(false);
  AccountService.clearState();
  localStorage.setItem('loginStatus','0'); 
  localStorage.removeItem('jwt');  
  localStorage.removeItem('username');
  localStorage.removeItem('authorities');
  localStorage.removeItem('expiration');
  this.userName.next(localStorage.getItem('username'));
  this.userAuthority.next(localStorage.getItem('authorities'));
  this.router.navigate(['/home']);
}

// geteri za 3 behavior subjecta koje koriste razne komponente

isLoged(){
  return this.loginStatus.asObservable();
}
getUserName(){
  return this.userName.asObservable();
}
getUserAuthorities(){
  return this.userAuthority.asObservable();
}



}
