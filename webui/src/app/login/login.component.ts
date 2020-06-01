import { Component, OnInit, ɵConsole } from '@angular/core';
import { AccountService } from '../services/account.service';
import * as jwt_decode from "jwt-decode";
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { stringify } from '@angular/compiler/src/util';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  insertForm:FormGroup;
  Username:FormControl;
  Password:FormControl;
  errorMessage:string;
  invalidLogin:boolean;

  constructor(private accountService : AccountService ,
              private router : Router ,
              private route : ActivatedRoute ,
              private formBuilder : FormBuilder
    ) { }

  ngOnInit() {

    this.Username=new FormControl('',[Validators.minLength(6),Validators.maxLength(50),Validators.required]);
    this.Password=new FormControl('',[Validators.minLength(6),Validators.maxLength(50),Validators.required]);
    this.insertForm=this.formBuilder.group({
      "Username":this.Username,
      "Password":this.Password
    });

    
  }

  onSubmit(){
    // promenjiva dobija pristup svim vrednostima u predmetnoj formi
    let userlogin = this.insertForm.value;
    
    // kao parametre metoda uzimamo vrednosti dva polja forme ( iz gore kreiranog objekta)
    this.accountService.signin(userlogin.Username,userlogin.Password).subscribe(
      result=>{
        this.invalidLogin = false;
        // ako je sve ok posle logovanja ide na home
        this.router.navigate(['/home']);
      }, 
      error=>{
        this.invalidLogin=true;
        this.errorMessage = error.error.message;
      }
    );

  }

  


}
