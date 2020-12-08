import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS, ɵHttpInterceptingHandler } from "@angular/common/http"
import { Injectable } from '@angular/core';
import { isObservable, observable, Observable, of, throwError } from 'rxjs';
import { catchError, concatMap, delay, flatMap, mergeMap, retry, retryWhen, take, tap } from 'rxjs/operators';


@Injectable()
export class BackendErrorInterceptor implements HttpInterceptor {

    retryLimit: number = 3;
    dellay:number = 1000;

    // VAZNO :
    // Uloga mu je da prosledi gresku koju ce global error handler dalje obradjivati ako je backend ok
    // i tacno odgovori sta mu je (ako greska nije medju navedenim u if), tj. da proba jos tri puta sa 
    // razmakom od 1 sekunde ako backend nije ok ( npr ako je ugasen server stici ce timeout 504) u nadi da ce
    // server proraditi. Time bi interceptor ispunio namenu da korisnik nedobije gresku ako server proradi.
    
    // VAZNO : moze da radi i razne druge stvari sto sam dole napomenuo tj. interceptor moze da programski 
    // odreaguje na gresku i eventualno je ispravi , ili nesto uradi pa je tek onda baci i sl.

    // ovako bi bilo kompletno resenje za hvatanje greske. ako req ode i response dodje bez greske sve se 
    // preskace i nema greske, ako je greska klijentska 400+ retryWhen odma baca gresku jer je if tacno, gresku
    // hvata catcherror i baca je pa ce je global error handler procesirati . ako je greska serverska 500+
    // (u ovom slucaju 500 ili 504) if je netacno dok broj pokusaja nedostigne limit, tj. retraywhen ce pokusati
    // ukupno retrylimit puta da posalje originalni request posle cega ce odustati (if ce biti tacno) i baciti gresku
    // a dalje isto kao i greske 400+ . Izmedju ubacujem delay 1 sekundu , sto dovodi do toga da se ponovni requesti
    // (drugi i treci) salju posle 1 sekunde (prvi ide odmah)
    // ovo je realan scenario jer ako je greska 400+ treba je odmah proslediti
    // ako je 500+ ima smisla probati jos par puta sa vremenskim razmakom u nadi da ce server vratiti trazeno
    // serveri obicno limitiraju broj pokusaja i razmak izmedju njih pa zato mora ovako
    // npr ako je server down status ce biti 504 , pa probam jos dva puta u nadi da ce server doci sebi
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {  
        let attempt: number = 0;    
        return next.handle(req).pipe(
            retryWhen((errors) => errors.pipe( 
                                               
                tap(error => {
                    if (++attempt >= this.retryLimit || (error.status !== 504 && error.status !== 500)){
                        throw error;
                    }
                }),
                delay(this.dellay)
            )),                
            catchError( (error: HttpErrorResponse) => {
                // ovde bi islo hendlanje klijentskih 400+ gresaka

                /* recimo preusmerimo klijenta na neku stranicu pa bacimo gresku
                if (error.status === 401 || error.status === 400 || error.status === 403) {
                    this.router.navigateByUrl('abort-access', { replaceUrl: true });
                }
                return throwError(error);
                */

                // ili uopste nebacimo gresku nego je ispravimo (npr. jwt token istekao stigne greska), nemoramo 
                // korisniku baciti gresku , nego pozovemo metodu da refresh token , pa ponovo posaljemo request

                // ovako samo bacam gresku
                return throwError(error);
            })            
        );           
    }    
}