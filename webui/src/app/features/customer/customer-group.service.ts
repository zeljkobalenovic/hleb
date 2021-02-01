import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ObservableStore } from '@codewithdan/observable-store';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { CustomerGroup } from 'src/app/core/model/customerGroup';
import { customerGroupQueryOptions, StoreState } from 'src/app/shared/store/store-state';

@Injectable({
  providedIn: 'root'
})
export class CustomerGroupService extends ObservableStore<StoreState> {
  
  

  apiUrl : string = '/api/customergroup'

  constructor( private http : HttpClient ) {
    super( { trackStateHistory : true , logStateChanges : true } );  
    // dok traje dev , posle samo super( {} ) bice po defaultu tj. false
  }

  // metode(klasicno) -- actions(state-managment)
  // komponenta/e komuniciraju sa backendom pozivanjem ovih metoda ( crud i slicno )

  getQueryOptions() {
    const state = this.getState();
    if ( state && state.customerGroupsQueryOptions ) {
      return of(state.customerGroupsQueryOptions);
    }
    else {
      const customerGroupsQueryOptions = {
        filter : '',
        currentPage : 1,        
        pageSize : 5
      }
      this.setState( { customerGroupsQueryOptions } , 'OPTIONS_DEFAULTS')
      return of(customerGroupsQueryOptions);
    }      
  }

  changeQueryOptions(data:string) {
    let customerGroupsQueryOptions = this.getState().customerGroupsQueryOptions;
    customerGroupsQueryOptions.filter = data;
    this.setState({ customerGroupsQueryOptions } , 'QUERY_OPTIONS_CHANGE');
    return this.query();
  }



  private fetchCustomerGroups() : Observable<CustomerGroup[]> {
    return this.http.get<CustomerGroup[]>(this.apiUrl)
        .pipe(
          map( customerGroups => {
            this.setState( { customerGroups } , 'GET_CUSTOMER_GROUPS' );
            return customerGroups;
          }),
          
        );
  }

  getAll() {
    const state = this.getState();
    // prvo iz kesa 
    if ( state && state.customerGroups ) {
      return of(state.customerGroups)
    }
    // ako nema u kesu sa backenda
    else {
      return this.fetchCustomerGroups()        
        .pipe(
          
        );
    }
  }

  getCustomerGroup(id: number) : Observable<CustomerGroup> {
    return this.http.get<CustomerGroup>(this.apiUrl + '/' + id).pipe(
      map( selectedCustomerGroup => {
        this.setState({selectedCustomerGroup} , 'CUSTOMER_GROUP_SELECTED');
        return selectedCustomerGroup;
      })
    )
  }

  add(customerGroup: CustomerGroup) : Observable<CustomerGroup[]> {
    return this.http.post<CustomerGroup>(this.apiUrl , customerGroup).pipe(
      switchMap( customerGroup => {
        // posle uspesnog doddavanja radim requery liste sa azurnim podatcima sa backenda
        return this.fetchCustomerGroups();
      })
    )
  }

  update(customerGroup: CustomerGroup) : Observable<CustomerGroup[]>{
    return this.http.put<CustomerGroup>(this.apiUrl + '/' + customerGroup.id , customerGroup).pipe(
      switchMap( customerGroup => {
        return this.fetchCustomerGroups();
      })
    )
  }

  
  delete(id: number) : Observable<CustomerGroup[]> {
    return this.http.delete(this.apiUrl + '/' + id).pipe(
      switchMap( () => {
        return this.fetchCustomerGroups();
      })
    )
  }

  query() {

    const data = this.getState().customerGroupsQueryOptions.filter
    const options = data ? {params :new HttpParams().set('prm1',data)} : {params :new HttpParams().set('prm1','')};
        
    return this.http.get<CustomerGroup[]>(this.apiUrl + '/query', options)
      .pipe(
        map( customerGroups => {
          this.setState( {customerGroups} , 'CUSTOMER_GROUP_QUERY');
          return customerGroups;
       })
      ).subscribe();
  }

  

}
