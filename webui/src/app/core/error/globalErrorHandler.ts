import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { MyError } from '../model/myError';
import { ErrorService } from './services/error.service';
import { LoggingService } from './services/logging.service';
import { NotificationService } from './services/notification.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

    // Error handling is important and needs to be loaded first.
    // Because of this we should manually inject the services with Injector.
    // Meni je radilo i sa klasicnim di , ali ima smisla
    constructor(private injector: Injector) {}

    /*
    constructor(private errorService : ErrorService,
                private notificationService: NotificationService,
                private loggingService: LoggingService) {}
    */

     
    handleError(error: Error | HttpErrorResponse): void {
        
        const errorService = this.injector.get(ErrorService);
        const loggingService = this.injector.get(LoggingService);
        const notificationService = this.injector.get(NotificationService);

        let myError : MyError;
        
        // ako je serverska-backend greska
        if (error instanceof HttpErrorResponse) {  
            myError = errorService.getServerMessage(error);               
        }
        // ako je klijentska greska
        else {
            myError = errorService.getClientMessage(error);
        }

        // zatim gresku logujemo i prikazemo korisniku
        loggingService.logError(myError);
        notificationService.showErrorNotification(myError);

        // tokom dev da se vidi u konzoli greska (originalna , a ne prilagodjena na myError)
        console.error(error);
        
    }
    
}