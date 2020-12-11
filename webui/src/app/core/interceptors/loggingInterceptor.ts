import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { finalize, tap } from "rxjs/operators";
import { LoggingService } from "../error/services/logging.service";

@Injectable()
export class LoggingInterceptor implements HttpInterceptor {

    constructor(private loggingService: LoggingService) {} 

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        let started: number  = Date.now();
        let httpResult: string;

        return next.handle(req).pipe(
            tap( 
                (event: HttpEvent<any>) => {
                    // event je ili HttpResponse ako je ok , 
                    if (event instanceof HttpResponse) {
                        httpResult = 'success';
                    } 
                },
                (error) => {
                    httpResult = 'failed';
                }
                
            ),
            // sad bez obzira dali je stigao respons ili errorresponse ide loggovanje
            finalize( () => {
                const elapsedTime:number = Date.now() - started;
                started = Date.now();
                const message = `${req.method} ${req.urlWithParams} ${httpResult.toUpperCase()} in ${elapsedTime} ms`;
                this.loggingService.logRequest(message);
            }
            )
        );
    }
    
}