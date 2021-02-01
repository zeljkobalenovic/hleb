import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ObservableStore } from '@codewithdan/observable-store';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { Customer } from 'src/app/core/model/customer';
import { customerQueryOptions, StoreState } from 'src/app/shared/store/store-state';

@Injectable({
  providedIn: 'root'
})
export class CustomerService extends ObservableStore<StoreState> {
    
  private apiUrl : string = '/api/customer'; 
  
  private defaultValues = {
    activeTab: "customer",
    customers: [], 
    selectedCustomer: null,
    pageSizes: [5,10,20],
    pageSize: 10,
    currentPage: 1,
    filters: ['','0','0']
  };  
  
  constructor( private http : HttpClient ) {
    super({trackStateHistory:true , logStateChanges:true});
  }

  getDefaultValues() {
    return this.defaultValues;
  }

  getQueryOptions(): Observable<customerQueryOptions> {
    const state = this.getState();
    if ( state && state.customerQueryOptions ) {
      return of(state.customerQueryOptions);
    }
    else {
      const customerQueryOptions = {
        filter : this.defaultValues.filters,
        currentPage : this.defaultValues.currentPage,
        pageSize : this.defaultValues.pageSize
      }
      this.setState({ customerQueryOptions }, 'OPTIONS_DEFAULT');
      return of(customerQueryOptions);
    }
  }

  setActiveTab(activeTab:string) : void {
    this.setState({activeTab} , 'ACTIVE_TAB_SET')
  }

  private fetchCustomers():Observable<Customer[]> {
    const fetchOptions = this.getState().customerQueryOptions;
    const options = {
                      params : new HttpParams()
                        .append('filterName' , fetchOptions.filter[0])
                        .append('filterGroup' , fetchOptions.filter[1])
                        .append('sortOptions' , fetchOptions.filter[2])
                        .append('page' , fetchOptions.currentPage.toString())
                        .append('pageSize' , fetchOptions.pageSize.toString())
                    };
    return this.http.get<Customer[]>(this.apiUrl,options).pipe(
        map( customers => {
          this.setState({customers} , 'CUSTOMERS_FETCH');
          let selectedCustomer : Customer | null;
          (customers.length===0) ? selectedCustomer=null : selectedCustomer=customers[0];
          this.setState({selectedCustomer} , 'CUSTOMER_SELECT');
          return customers;
        })
    );                
  }

  setSelectedCustomer(id: number) {
    let selectedCustomer : Customer = this.defaultValues.selectedCustomer;
    const state : StoreState = this.getState();
    if (state && state.customers && state.customers.length) {
      const filteredCustomer : Customer[] = state.customers.filter(customer => customer.id === id);
      (filteredCustomer.length===1) ? selectedCustomer=filteredCustomer[0] : selectedCustomer=this.defaultValues.selectedCustomer
      
    }
    this.setState({selectedCustomer} , 'CUSTOMER_SELECTED');
  }

  getAll() : Observable<Customer[]> {
    const state = this.getState();
    if ( state && state.customers ) {
      return of(state.customers);
    }
    else {
      return this.fetchCustomers()
        .pipe(
       
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
        
      );
  }

  add(customer : Customer) : Observable<Customer[]> {
    console.log("radim add customer");
    console.log(customer);
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
          // this.setState( { selectedCustomer : null} , 'CUSTOMER_DELETED');
          return this.fetchCustomers(); 
        })
      )
  }

  
}
