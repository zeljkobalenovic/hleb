import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MyError } from '../../model/myError';


@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor() { }

  // error service ima dve metode koje vracaju klijentsku i/ili serversku gresku u obliku koji zelim

  getClientMessage(error : Error) : MyError {
    const myMessage : string = error.message;
    const myStack : any = error.stack;
    // JAVASCRIPT objekat moze ovako (zato sto svaki js je validan ts)
    // const myError : MyError = {message : myMessage , stack : myStack }     
    // TYPESCRIPT objekat ovako ide 
    // za pravljenje koristi constructor zato je IZUZETNO VAZNO voditi racuna kod prosledjivanja parametara 
    // constructoru (3 parametra , a samo prvi je obavezan ostala dva nisu). Ako ne stavim null za drugi parametar
    // nego navedem samo dva objekat myError bi myStack ubacio u subErrors sto ne zelim
    const myError : MyError = new MyError(myMessage,null ,myStack);            
    return myError;
  } 

  getServerMessage(error : HttpErrorResponse) : MyError {
    // od serverske greske hocu samo message i subErrors
    // kod npr. http backend error interceptora greska ce uvek biti ovakva i genericka je u error.mesage
    // nju upisujem ako nema specificne (recimo server je ugasen stici ce 504 ) i nema specificne greske
    // ali ako je server ok i npr. req ne valja u error.error.message ce biti bolje objasnjenje pa to upisujem
    // npr customer sa tim imenom vec postoji stize 400 bad request , a moja poruka je u error.error.message
    const myMessage : string = (error.error.message) ? error.error.message : error.message;
    const mySubErrors : string[] = error.error.subErrors;
    // const myError : MyError = {message : myMessage , subErrors : mySubErrors }
    // Ovako slucajno moze jer prva dva parametra popunjavamo sa ove dve konstante treci stack  ce biti undefined
    // const myError : MyError = new MyError(myMessage,mySubErrors);
    // ovako je bolje nema mogucnosti greske , a treci stack ce biti null, a ne undefined 
    const myError : MyError = new MyError(myMessage,mySubErrors,null);
    return myError;
  }

  
}
