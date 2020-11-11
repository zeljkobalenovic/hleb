import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ObservableStore } from '@codewithdan/observable-store';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { CustomerGroup } from 'src/app/core/model/customerGroup';
import { StoreState } from 'src/app/shared/store/store-state';

@Injectable({
  providedIn: 'root'
})
export class CustomerGroupService extends ObservableStore<StoreState> {

  apiUrl : string = '/api/customergroup'

  constructor( private http : HttpClient ) {
    super( { trackStateHistory : true , logStateChanges : true } );  
    // dok traje dev , posle je super( {} ) bice po defaultu tj. false
  }

  // metode(klasicno) -- actions(state-managment)
  // komponenta/e komuniciraju sa backendom pozivanjem ovih metoda ( crud i slicno )

  private fetchCustomerGroups() {
      return this.http.get<CustomerGroup[]>(this.apiUrl)
        .pipe(
          map( customerGroups => {
            this.setState( { customerGroups } , 'GET_CUSTOMER_GROUPS' );
            return customerGroups;
          }),
          // catchError(this.handleError)
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
          catchError(this.handleError)
        );
    }
  }

  add(customerGroup:CustomerGroup) {
    
    const cgtoAdd : CustomerGroup = { name : "grupa dodata iz angulara 5" }
    return this.http.post<any>(this.apiUrl , cgtoAdd)
      .pipe(
        switchMap( result => {         
          return this.fetchCustomerGroups()
        }) ,
        catchError(this.handleError)
      );
  }

  private handleError(error: any) {
    console.error('server error:', error);
    if (error.error instanceof Error) {
        const errMessage = error.error.message;
        return Observable.throw(errMessage);
    }
    return Observable.throw(error || 'Server error');
  }

}
