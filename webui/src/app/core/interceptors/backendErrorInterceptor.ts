import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS, ɵHttpInterceptingHandler } from "@angular/common/http"
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';



export class BackendErrorInterceptor implements HttpInterceptor {
    
    // u slucaju da dodje do greske na serveru tj. ako je response tipa HttpErrorResponse treba da odreaguje
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // salje request
        return next.handle(req).pipe(
            // kad stigne response prvo operator retry(koliko puta), ovaj operator u pipe lancu lovi error response
            // originalnog req i u slucaju da ga uvati ponovo subscribuje originalni req i salje isti req na server
            // ovako se standardno proba pre nego sto se stvarno baci error
            retry(3),
            // operator catchError hvata gresku tj. aktivira se ako je response HttpErrorResponse tipa
            catchError( (error : HttpErrorResponse) => {
                // sada na osnovu HttpErrorResponse moze nesto da se preduzme ili da se baci greska
                // recimo ako je error da je istekao jwt token moze se pozvati metoda koja ce uraditi token refresh 
                // pa sa novim tokenom ponovo poslati req , a ne baciti gresku
                // zasad nista ne radim nego bacim gresku
                return throwError(error);    
            } )
        );
    }
    
}