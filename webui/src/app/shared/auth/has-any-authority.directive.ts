import { Directive, TemplateRef, ViewContainerRef, Input, OnDestroy } from '@angular/core';
import { AccountService } from 'src/app/services/account.service';
import { Subscription } from 'rxjs';


// ova direktiva ce ubuduce da se koristi za uslovno dodavanje elementa ukoliko trenutno logovani user
// ima zadovoljavajuci authority (potrebni authority ce biti string ili niz stringova - koji se uporedjuju sa
// authority koje ima logovani korisnik i ako su ok nadjene predmetni html se renderuje tj. dozvoljen je).
// primer koriscenja opcije menija, dugmad za add ili edit ili delete necega i slicno.
// prva upotreba da u meniju stavka reports bude prikazana samo za korisnike koji imaju prava da rade sa reportsima


@Directive({
  // ovo stavljamo na html koji hocemo uslovno da prikazemo npr : [HasAnyAuthority] = "['REPORT_READ', ... jos clanova niza ]"
  // stavicu na stavku menija report (treba da se renderuje samo ako je logovan admin ili generalmanager)
  selector: '[HasAnyAuthority]'
})
export class HasAnyAuthorityDirective implements OnDestroy { 

  // ovaj niz sadrzi trazene authoritije za neki html element
  private authorities: string[] = [];
  // ovaj subscription je na observable user authority napravljene u accountservice 
  // userAuthority je behaviour subject , za koriscenje istog imamo metodu getUserAuthorities() koja ga vraca kao observable
  // to je sablon uvek . Komponenta koja hoce to da koristi ( u ovom slucaju ova direktiva se subscribuje na to cime uvek 
  // dobija azurnu listu userovih rola , kad se user izloguje ili se ulogiuje novi ovo se menja i emituje svim subscriberima)
  private userAuthoritySubscription : Subscription;

  // iz account service cu procitati authoritije logovanog usera zato di
  // tempateref i viewcontainerref su klasika za strukturne direktive u angularu - ova je strukturna
  // ono sto je templateref (neki html) ce biti renderovano u containerref (sama direktiva)
  constructor(private accountService : AccountService, private templateRef : TemplateRef<any> , private viewContainerRef : ViewContainerRef ) { }
  
  // input oznacava nesto sto iz parent componente (u ovom slucaju je to nav-menu dolazi u child componentu - u ovom slucaju 
  // to je ova direktiva , a mogla je biti i neka komponenta ili sevis)
  // NAPOMENA obrnut slucaj je output kada nesto iz child komponente ide na parent komponentu
  // to obicno bude neki event - pritisnuto dugme ili slicno - otom potom
  // ovo je komplikovaniji slucaj inputa (zato sto direktiva nije samo korisnik podatka tj nece samo procitati trazene role
  // nego ce i na osnovu biznis logike istu setovati tj. sprovesti neku akciju )
  @Input()
  set HasAnyAuthority(value : string | string[]) {
    // posto ulaz moze biti string ili niz stringova prvo ako je string pretvaramo ga u niz stringova , ako je niz nista
    this.authorities = typeof value === 'string' ? [value] : value ;

    // subscribe na promene rola i pozvati metodu updateview svaki put kad se role promene
    this.userAuthoritySubscription=this.accountService.getUserAuthorities().subscribe(data=>this.updateView(data , this.authorities))
  }

  updateView(userAuthority : string, directiveAuthority : string[]) {
    // userAuthority je string sa svim userovim authoritijima razdvojenim zarezom , a directiveauthority je niz stringova 
    // (obicno samo jedan string u nizu ) - ide provera dali je clan niza substring od userauthority , ako jeste user ima 
    // privilegije i html element se crta , ako ne necrta se
    let view : boolean = false ;
    if (userAuthority && directiveAuthority) {
      for ( let item of directiveAuthority) {
        if ( userAuthority.includes(item)) {
          view = true;
          // mozemo odma izaci ako se nadje prvo poklapanje tj. user ima potreban authority
          break;
        }
      }
    }
    // console.log(view);
    // ako su se nasli view je true i crta html , ako ne brise element 
    if ( view ) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainerRef.clear();
    }
  }

  // best practic je da komponenta  unubscribuje sve sto je subscribovala kad vise nije potrebno (kad se komponenta ugasi) 
  ngOnDestroy(): void {
    if (this.userAuthoritySubscription) {
      this.userAuthoritySubscription.unsubscribe();
    }
  }

}


