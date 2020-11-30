import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CustomerGroup } from 'src/app/core/model/customerGroup';
import { CustomerGroupService } from '../customer-group.service';



@Component({
  selector: 'app-customer-group-edit',
  templateUrl: './customer-group-edit.component.html',
  styleUrls: ['./customer-group-edit.component.css']
})
export class CustomerGroupEditComponent implements OnInit, OnDestroy {

  customerGroup : CustomerGroup;

  customerGroupForm : FormGroup = this.formBuilder.group({
    id : [],
    name : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]]
  });

  private subscription : Subscription = new Subscription();  

  constructor(private customerGroupService: CustomerGroupService,
              private formBuilder : FormBuilder,
              private router : Router,
              private route : ActivatedRoute) { }
  

  ngOnInit() {
    // kad lista pozove update-delete prosledjuje parametar id u ruti
    // znaci ako ga ima treba ucitati customergroup koji hocu da update-delete
    // parametri rute su uvek tipa {name1:value1 , ....} gde su i name i value string 
    // zato sa + forsiram pretvaranje stringa u number 
    const id : number = +this.route.snapshot.paramMap.get('id');
    // ako postoji id to znaci da je trazen update-delete odredjenog (po id)
    // zato ga prvo dobavljamo (sa backenda da bude aktuelno-zbog multiuser)
    if (id) {
      this.subscription = this.customerGroupService.getCustomerGroup(id).subscribe( customerGroup => {
        if ( customerGroup ) {
          // ako ga nadje setujem properties
          this.customerGroup = customerGroup;
          // popunjavam polja forme sa vrednostima objekta (patch to radi automatski ako se imena poklapaju)
          this.customerGroupForm.patchValue(this.customerGroup);
        }
      });
    }
  }

  onSubmit() {
    if ( this.customerGroupForm.valid ) {
      // ...spread operator customerGroup ima ista polja kao i forma ovo donje je skracena sintaksa 
      /* umesto peske upisivanja jednog po jednog moze sve odjednom 
      this.customerGroup.id=this.customerGroupForm.value('id');
      this.customerGroup.name=this.customerGroupForm.value('name');
      .... */
      const customerGroupValue : CustomerGroup = { ...this.customerGroup , ...this.customerGroupForm.value }
      if (customerGroupValue.id) {
        // ako id postoji update postojeceg
        this.update(customerGroupValue);
      } 
      else {
        // ako id ne postoji add novog
        this.add(customerGroupValue);
      }
    } 
  }

  add(customerGroup : CustomerGroup) {
    this.subscription = this.customerGroupService.add(customerGroup).subscribe( () => {
      this.navigateHome();
    });
  }

  update(customerGroup : CustomerGroup) {
    this.subscription = this.customerGroupService.update(customerGroup).subscribe ( () => {
      this.navigateHome();
    })
  }

  delete() {
    this.subscription = this.customerGroupService.delete(this.customerGroup.id).subscribe( () => {
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
