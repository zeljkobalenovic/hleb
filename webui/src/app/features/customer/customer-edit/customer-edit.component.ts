import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, Subscription } from 'rxjs';
import { Customer } from 'src/app/core/model/customer';
import { CustomerService } from '../customer.service';



@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css']
})
export class CustomerEditComponent implements OnInit, OnDestroy {

  private _selectedCustomer : Customer;

  @Input()
  get selectedCustomer() : Customer {
    return this._selectedCustomer;
  }

  set selectedCustomer( value : Customer ) {
    if (value) {
      this._selectedCustomer=value;
      this.customerForm.patchValue(this._selectedCustomer);
     }
  }

  

  customerForm : FormGroup = this.formBuilder.group({
    id : [],
    name : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]],
    code : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]],
    streetAndNumber : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]],
    postcode : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]],
    city : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]],
  });

  private subscription : Subscription = new Subscription();  

  constructor(private customerService: CustomerService,
              private formBuilder : FormBuilder,
              private router : Router,
              private route : ActivatedRoute) { }
  

  ngOnInit() {      
       
  }
  

  onSubmit() {
    if ( this.customerForm.valid ) {
      // ...spread operator customerGroup ima ista polja kao i forma ovo donje je skracena sintaksa 
      /* umesto peske upisivanja jednog po jednog moze sve odjednom 
      this.customerGroup.id=this.customerGroupForm.value('id');
      this.customerGroup.name=this.customerGroupForm.value('name');
      .... */
      const customerValue : Customer = { ...this._selectedCustomer , ...this.customerForm.value }
      if (customerValue.id) {
        // ako id postoji update postojeceg
        this.update(customerValue);
      } 
      else {
        // ako id ne postoji add novog
        this.add(customerValue);
      }
    } 
  }

  add(customer : Customer) {
    this.subscription = this.customerService.add(customer).subscribe( () => {
      this.navigateHome();
    });
  }

  update(customer : Customer) {
    this.subscription = this.customerService.update(customer).subscribe ( () => {
      this.navigateHome();
    })
  }

  delete() {
    this.subscription = this.customerService.delete(this._selectedCustomer.id).subscribe( () => {
      this.navigateHome();
    })
  }

  navigateHome() {
    this.router.navigate(['/customers']);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}

