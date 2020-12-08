
import { Component, OnInit } from '@angular/core';
import { combineLatest, merge, Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Customer } from 'src/app/core/model/customer';
import { CustomerGroup } from 'src/app/core/model/customerGroup';
import { QueryOptions } from 'src/app/shared/store/store-state';
import { CustomerGroupService } from '../customer/customer-group.service';
import { CustomerService } from './customer.service';

@Component({
  selector: 'app-customer-container',
  templateUrl: './customer-container.component.html',
  styleUrls: ['./customer-container.component.css']
})
export class CustomerContainerComponent implements OnInit {

  // ono sto prikazujem u dumb komponenti/ama ovde nabavljam iz state/servisa , a njima ih prosledjujem kroz @input
  // ono sto je akcija korisnika nabavljam od dumb componenti putem @output i na osnovu eventa menjam state 
  // pozivajuci metode servisa ( ovaj kontejner je zajednicki za customer i customer group )
  // sva stanja su observable , a subscribe ide u html pomocu async pipe

  title : string = 'Customer and Customer Group';
  activeTab$ : Observable<string>;

  customerGroups$ : Observable<CustomerGroup[]>
  selectedCustomerGroup$ : Observable<CustomerGroup>;
  customerGroupsQueryOptions$ : Observable<QueryOptions>;
  
  customers$ : Observable<Customer[]>;
  selectedCustomer$ : Observable<Customer>;
  

  constructor(private customerService : CustomerService , private customerGroupService : CustomerGroupService){ }

  ngOnInit() {
    // inicijalizujem + pretplatim na kasnije izmene za sve state
    // inicijalno obe liste napunim , a selektovano je null, queryoptions default

    this.activeTab$=this.customerService.stateChanged.pipe(
      map( state => {
        if (state && state.activeTab) {
          return state.activeTab;
        } 
        else {
          return 'customergroup';
        }
      })
    )

    this.customerGroupsQueryOptions$ = merge(
      this.customerGroupService.getQueryOptions(),
      this.customerGroupService.stateChanged.pipe(
        map( state => {
          if (state) {
            return state.customerGroupsQueryOptions;
          }
        })
      )
    )
    
    this.customers$ = merge(
      this.customerService.getAll(),
      this.customerService.stateChanged.pipe(
        map( state => {
          if (state) {
            return state.customers;
          }
        })
      )
    );

    
    this.customerGroups$ = merge(
      this.customerGroupService.getAll(),      
      this.customerGroupService.stateChanged.pipe(
        map( state => {
          if (state) {
            return state.customerGroups;
          }
        })
      )
    );



    this.selectedCustomer$ = this.customerService.stateChanged.pipe(
      map( state => {
        if (state) {
          return state.selectedCustomer;
        }
      })
    );

    this.selectedCustomerGroup$ = this.customerGroupService.stateChanged.pipe(
      map( state => {
        if (state) {
          return state.selectedCustomerGroup;

        }
      })
    );

    

  }  

  setActiveTab(activeTab: string){
    this.customerService.setActiveTab(activeTab);
  }

  selectCustomer(id : string) {
    this.customerService.setSelectedCustomer(id);
  }

  changeQueryOptions( data : string ) {
    // posto se promenio query options treba da setuje query options state
    return this.customerGroupService.changeQueryOptions(data);
    
    
  }

}
