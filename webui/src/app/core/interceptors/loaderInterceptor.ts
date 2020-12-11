import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { delay, finalize } from 'rxjs/operators';
import { LoaderService } from '../error/services/loader.service';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {

    activeRequest: number = 0;

    constructor (private loaderService: LoaderService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        if (++this.activeRequest === 1) {
            this.loaderService.showLoader(true);
        }        
        return next.handle(req).pipe(
            // delay(5000),
            finalize ( () => {
                if (--this.activeRequest===0){
                    this.loaderService.showLoader(false);
                }            
            })
        )
        
    }
}