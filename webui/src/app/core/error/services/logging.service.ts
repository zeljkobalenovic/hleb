import { Injectable } from '@angular/core';
import { MyError } from '../../model/myError';
// import { SlackService } from './slack.service'

@Injectable({
  providedIn: 'root'
})
export class LoggingService {

  // constructor(private slackService: SlackService) { }

  logError(error : MyError) {
    // samo za test - ovako NE RADITI - samo message upisujem na conzolu
    console.log("Loging service : " + error.message);
  }

  /*  
  logError(error : MyError) {
    // ovako bi trebalo koristeci specijalizovan servis (instaliramo , inicijalizujemo u konstruktoru i koristimo)
    this.slackService.postErrorOnSlack(error);    
  }
  */

  // isto za npr loggovanje http iz logging interceptora (loguje request,rezultat i vreme trajanja 
  logRequest(loggText: string) {
    console.log(loggText);
  }
}
