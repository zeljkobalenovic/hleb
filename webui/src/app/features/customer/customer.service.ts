import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ObservableStore } from '@codewithdan/observable-store';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { Customer } from 'src/app/core/model/customer';
import { StoreState } from 'src/app/shared/store/store-state';

@Injectable({
  providedIn: 'root'
})
export class CustomerService extends ObservableStore<StoreState> {
  
  
  apiUrl : string = '/api/customer'
  constructor( private http : HttpClient ) {
    super({trackStateHistory:true , logStateChanges:true});
  }

  setActiveTab(activeTab:string) : void {
    this.setState({activeTab} , 'ACTIVE_TAB_SET')
  }

  setSelectedCustomer(id: string) {
    const state : StoreState = this.getState();
    if (state && state.customers && state.customers.length) {
      const selectedCustomer : Customer = state.customers.filter(customer => customer.id === +id)[0];
      this.setState({selectedCustomer} , 'CUSTOMER_SELECTED');
    }
    else {
      const selectedCustomer : Customer = state.customers[0];
      this.setState({selectedCustomer} , 'CUSTOMER_SELECTED');
    }    
  }

  private fetchCustomers() : Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrl)
        .pipe(
          map( customers => {
            this.setState({customers} , 'CUSTOMERS_RELOADED');
            return customers;
          }) ,
          // handle error
        );
  }

  getAll() : Observable<Customer[]> {
    const state = this.getState();
    if ( state && state.customers ) {
      return of(state.customers);
    }
    else {
      return this.fetchCustomers()
        .pipe(
          // error handler
        )
      
    }
  }

  getCustomer(id: number) : Observable<Customer> | null {
    return this.getAll()
      .pipe(
        map( customers => {
          let filteredCustomers = customers.filter( customers => customers.id === id );
          const selectedCustomer = ( filteredCustomers && filteredCustomers.length) ? filteredCustomers[0] : null;
          this.setState( {selectedCustomer} , 'CUSTOMER_SELECTED');
          return selectedCustomer;
        }) ,
        // handle error
      );
  }

  add(customer : Customer) : Observable<Customer[]> {
    return this.http.post<Customer>(this.apiUrl , customer)
    // vratice customera ili server error
    // sa tim customerom necu nista , nego hocu da mi lista opet bude kao na serveru zato reload
    // koji ce postaviti state customers ( za listu customera ) cime ce biti uparena sa backendom
    // VAZNO : ovo je pesimisticki scenario ( listu osvezavam sa backenda )
    //         moze   optimisticki ( dodam lokalno u listu preko set state customers ), brze je jer neide
    //         na backend , ali ako ne uspe mora se rolbackovati state ... - komplikovanije je   
      .pipe(
        switchMap( customer => {
          return this.fetchCustomers();
        }),
        // handle error
      );
  }

  update(customer : Customer) : Observable<Customer[]> {
    return this.http.put<Customer>(this.apiUrl + '/' + customer.id , customer)
      .pipe(
        switchMap ( customer => { 
          this.setState( {selectedCustomer : customer } , 'CUSTOMER_UPDATED')
          return this.fetchCustomers();
        })
      )

  }

  delete(id : number) : Observable<Customer[]> {
    return this.http.delete(this.apiUrl + '/' + id )
      .pipe(
        switchMap ( () => {
          // customers se moze takodje odmah setovati  lokalno, ja cekam reload koji ce to obaviti
          this.setState( { selectedCustomer : null} , 'CUSTOMER_DELETED');
          return this.fetchCustomers(); 
        })
      )
  }

  
}
