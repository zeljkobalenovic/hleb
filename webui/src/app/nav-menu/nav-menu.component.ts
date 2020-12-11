import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoaderService } from '../core/error/services/loader.service';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css']
})
export class NavMenuComponent implements OnInit {

  constructor(private accountService:AccountService , private loaderService:LoaderService) { }

  // prikazivanje stavki u meniju u zavisnosti od logovanog korisnika 
  // sve sto je potrebno pripremio sam u account servisu , imam tri behavior subjecta i tri getera za citanje istih
  // naming convention je da se observable promenjive pisu sa dolar na kraju 
  // ako nema logovanog usera hocu da se vidi samo home,login i register
  // ako ima logovanog usera hocu da se nevidi vise login i register , a da se vidi logout i username 
  // + da se vide dozvoljene stavke home, order, product, customer , a reports samo ako userrola dozvoljava

  showLoader$: Observable<boolean>;
  LoginStatus$ : Observable<boolean>;
  UserName$ : Observable<string>;
  UserAuthority : string;
//  UserAuthority$ : Observable<string>;

  // VAZNO - ove 3 promenjive su observable tipa i ne mogu se direktno koristiti nego se propuste kroz pipe async 
  // kao sto je u html objasnjeno, tj. pipe napravi novu promenjivu sa vrednoscu observable promenjive , a prostog tipa
  // boolean ili string zavisi sta je observable ili
  // vazno - moze i drugi nacin u nginitu se subskrajbujem na behavior subject , pa ga onda koristim kao obicnu promenjivu
  // kao u primeru sa userauthority - onda netreba pipe async oba nacina rade 
  // VAZNO - moj accountservice je sa ta tri ( isloged, username i userauthority ) pandan userdetails u spring security tj.
  // i sve buduce komponente ce na ovakav nacin gledati sta se prikazuje korisniku , a sta ne
  

  ngOnInit() {

    this.showLoader$=this.loaderService.stateChanged.pipe(
      map( state => {
        if (state && state.showLoader) {
          return state.showLoader;
        } 
        else {
          return false;
        }
      })
    )

    this.LoginStatus$=this.accountService.isLoged();
    this.UserName$=this.accountService.getUserName();
  //  this.UserAuthority$=this.accountService.getUserAuthorities();
    this.accountService.getUserAuthorities().subscribe(result=> {
      (result ? this.UserAuthority=result : this.UserAuthority='')
    });
  }

  onLogout() {
    this.accountService.signout();
  }

}
