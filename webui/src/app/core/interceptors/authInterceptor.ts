import { DatePipe } from '@angular/common';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { EMPTY, Observable } from 'rxjs';
import { catchError, delay, tap } from 'rxjs/operators';
import { AccountService } from 'src/app/services/account.service';
import { NotificationService } from '../error/services/notification.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private authService: AccountService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        // ako req ide na /api/auth login ili registracija ne treba jwt 
        // ako req ide van api takodje netreba jwt (recimo logovanje errora na neki slack ili slicno)
        // zapravo jwt dodajemo SAMO ako ide na api deo koji nije auth
        if ( !req.url.startsWith('/api') || req.url.startsWith('/api/auth')) {
            return next.handle(req);
        }

        // Ako jwt token postoji i nije istekao salje req sa dodatim auth headerom
        if ( localStorage.getItem('jwt') && (new Date()<new Date(+localStorage.getItem('expiration')*1000))) {
            return next.handle(req.clone({headers: req.headers.set('Authorization', 'Bearer ' + localStorage.getItem('jwt'))}));
        }         

        // Ako jwt ne postoji ili je istekao radim logout + error koji ce global error hendlati i obavestiti korisnika
        this.authService.signout();
        throw new Error("Problem sa autentikacijom (token nedostaje ili je istekao. Molimo Vas da se ponovo logujete");    

        
    }
    
}