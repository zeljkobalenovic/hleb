import { Component, OnInit, ViewChild, TemplateRef, ViewChildren } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, AbstractControl, ValidatorFn, Validators, FormArray } from '@angular/forms';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
// ove dve stvari mi trebaju za modal ( 7/3 KORAK - service hendla evente sa modalom show i hide , modalref povezuje template za modal
// - kojih moze biti i vise , ali ovde ce biti samo jedan - modal template dodajem u html komponente - 7/4 KORAK)
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

// REACTIV FORM - VAZNO (sve je u dodatku 05 - AngularReactiveForm.txt)
export class RegisterComponent implements OnInit {

  // KORAK 1 
  registerForm:FormGroup;
  username:FormControl;  // value ove kontrole je vrednost koja se salje na server
  password:FormControl;
  cpassword:FormControl;
  email:FormControl;
  cemail:FormControl;
  roles:FormArray;    // value ove kontrole je niz stringova koji se salje na server

  
  // KORAK 1A (sada hardkodovano , ovde je to ok , ali u praksi se inicijalizuje sa servera u ngOnInit metodi)
  alowedroles : Array<any> = [
    {value:"USER" , description:"USER "},
    {value:"EMPLOYEE" , description:"EMPLOYEE - za zaposlene (zahteva aktivaciju)"},
    {value:"MANAGER" , description:"MANAGER - za zaposlene (zahteva aktivaciju)"},
    {value:"GENERALMANAGER" , description:"GENERALMANAGER - za zaposlene (zahteva aktivaciju)"},
    {value:"ADMIN" , description:"ADMIN - za zaposlene (zahteva aktivaciju)"},
  ];
  // KORAK 1B
  invalidRegister:boolean;  // ako registracija uspe false , ako error true
  // za modal poruke sa servera 
  modalRef : BsModalRef;    // 7/6 za modal
  // glavna poruka ( da li je registracija ok ili nije - namestim je u zavisnosti sta stigne sa servera u subscribe signupa)
  registerMessage:string;   
  // ako stigne ok - uz glavnu poruku hocu da ispisem u modalu i korisnika koji je registrovan (stize kao json) 
  // ovo je za svrhe testa modala - nije neophodno , ali da vezbamo - u principu se user posebno definise kao klasa
  // pa se moze koristiti na vise mesta , ali ovde ovako definisem objekat slican jsonu koji stize
  modalid:number;
  modalusername:string;
  modalemail:string;
  modalactive:string;
  modalroles:string[];
  // ako stigne error
  modalerror:string;
  modalerrorlist:string[];     

  // KORAK 7/5 - dekorator koji povezuje html template za modal sa kodom ( VAZNO - moze biti vise modal template u html razlicitih
  //             u tom slucaju bi koristili dekorator @ViewChildren , kad je samo jedan @ViewChild(id koji smo dali u html - ja sam
  //             dao #template , a drugi parametar zasad ovako - ima svrhu , ali je nov u angular8 ))
  @ViewChild('template',{static:false}) modal :TemplateRef<any>;
  // TemplateRef je iz angular/core i to je tip objekta koji ( nas modal je objekat tog tipa )
  

  

  constructor(
    // KORAK 2 - potrebni servisi i helperi
    private accountService:AccountService,
    private formBuilder:FormBuilder,
    private router:Router,
    private modalService:BsModalService  // 7/7 za modal
  ) { }


  

  // KORAK 3 - VALIDACIJE - VAZNO !!! - ovde idu custom validacije (sve je u dodatku 05 - AngularReactiveForm.txt)
  // ovaj custom validator ispituje match dva polja koja prosledimo u parametrima
  MatchValidator(controlName:string,matchingControlName:string) : ValidatorFn {
  return(group: FormGroup) : { [key:string] : boolean} | null => {
      const control = group.controls[controlName];
      const matchingControl = group.controls[matchingControlName];
      // return if another validator has already found an error on the matchingControl
      if (matchingControl.errors && !matchingControl.errors.mustMatch) {  return ;}
      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({mustMatch:true})
      } else {
        matchingControl.setErrors(null)
      }              
    };
  }

  ngOnInit() {
    // KORAK 4 inicijalizacija forme  
    // u konstruktoru prvi parametar je inicijalna vrednost (meni sve prazno, ali se moze podesiti), niz validatora posle
    this.username=new FormControl('',[Validators.required, Validators.minLength(6), Validators.maxLength(50)]);
    this.password=new FormControl('',[Validators.required, Validators.minLength(6), Validators.maxLength(50)]);
    this.cpassword=new FormControl('',[Validators.required]);
    this.email=new FormControl('',[Validators.required,Validators.email]);
    this.cemail=new FormControl('',[Validators.required]);
    this.roles=new FormArray([],Validators.required)

    this.registerForm = this.formBuilder.group({
        "username":this.username,      // ime kontrole na html : ime varijable u ts
        "password":this.password,
        "cpassword":this.cpassword,
        "email":this.email,
        "cemail":this.cemail,
        "roles":this.roles      // formarray se inicijalizuje sa alowedroles , a value mu je [], zatim se popunjava sa cekiranim
                                // VAZNO roles je prazan niz posle inicijalizacije
    })
    this.registerForm.setValidators([this.MatchValidator("password","cpassword")
                                    ,this.MatchValidator("email","cemail")]);

    
  }


  // KORAK 6 - OPCIONO punjenje ili praznjenje roles sa alowedroles vrednostima kad se neka cekira ili odcekira ( hvata event dole )
  onCheckboxChange(e) {
    this.roles = this.registerForm.get('roles') as FormArray;
    // kad se neki iz forme cekira , dodaje se u postojeci niz
    if (e.target.checked) {                                 //  if je ako smo neki oznacili - onda ga doda u array
      this.roles.push(new FormControl(e.target.value));
    } else {                                                //  else je ako smo neki odcekirali - onda ga brise iz arraya
      let i: number = 0;
      // prolazi kroz sve napravljene kontrole pomocu alowedroles i trazi onu koja je rascekirana (pomocu njenog eventa)
      this.roles.controls.forEach((item: FormControl) => {
        // kad je nadje brise je iz niza 
        if (item.value == e.target.value) {
          this.roles.removeAt(i);
          return;
        }
        i++;
      });
    }
    // console.log(this.roles.value);  // za proveru sta ide na server - provereno ide ok
  }

  
  // glavna metoda koja salje sve na server i obradjuje reaultat 
  onSubmit(){
    // promenjiva dobija pristup svim vrednostima u predmetnoj formi
    let userregister = this.registerForm.value;
    // kao parametre metoda uzimamo vrednosti 7 polja forme ( iz gore kreiranog objekta)
    this.accountService.signup(userregister.username
                              ,userregister.password
                              ,userregister.cpassword
                              ,userregister.email
                              ,userregister.cemail
                              ,userregister.roles
                              ).subscribe(
      result=>{
        // ako je rezultat ok stize nam json sa novoregistrovanim userom
        this.invalidRegister = false;
        this.registerMessage = "User registration is successfuly";
        this.modalid=result.id;
        this.modalusername=result.username;
        this.modalemail=result.email;
        this.modalactive=result.active == true ? 'User is active' : 'Please call admin to activate this account';
        this.modalroles=[];
        for (let index = 0; index < result.roles.length; index++) {
          this.modalroles.push(result.roles[index].name);          
        }
        this.modalRef = this.modalService.show(this.modal);        
        // console.log(result);   // za proveru   
        // kad sve prodje ok ide automatski na login     
        this.router.navigate(['/login']);
      }, 
      
      error=>{
        // ako rezultat nije ok stize nam json sa greskama 
        this.invalidRegister=true;
        this.registerMessage = "User registration failed";
        // VAZNO - kad je rezultat ok ( stigne http 200 - odmah imamo rezultat u nasem result - tj objekat koji posalje server primimo
        // odmah isti takav .)
        // AKO REZULTAT NIJE OK ( stigne http 400+ ) - pravi se error objekat , VAZNO  nas error koji smo poslali sa servera je samo
        // deo tog errora koji dobijamo sa error.error ( nas error se vrapuje ukupnim objektom greske )
        // VAZNO - greska koju sam ja poslao ( moj error objekat ) je u stvari samo jedna stavka ukupnog error objekta i to error.error
        this.modalerror=error.error.message;
        this.modalerrorlist=[];
        // ako postoje greske validacije onda i lista suberrora
        if(error.error.subErrors) {
          for (let index = 0; index < error.error.subErrors.length; index++) {          
            this.modalerrorlist.push(error.error.subErrors[index].message);          
          }
        }        
        this.modalRef = this.modalService.show(this.modal);      
        // console.log(error);   // za proveru (!!! kad se zavrsi ukljuci ponovo klijent validatore)        
      }
      
    );       
  }
}
