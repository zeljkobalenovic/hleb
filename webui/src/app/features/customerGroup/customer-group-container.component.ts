import { Component, OnInit } from '@angular/core';
import { merge, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CustomerGroup } from 'src/app/core/model/customerGroup';
import { CustomerGroupService } from './customer-group.service';

@Component({
  selector: 'app-customer-group-container',
  templateUrl: './customer-group-container.component.html',
  styleUrls: ['./customer-group-container.component.css']
})
export class CustomerGroupContainerComponent implements OnInit {

  title = 'Customer Group';
  customerGroups$ : Observable<CustomerGroup[]>;

  constructor( private customerGroupService : CustomerGroupService) { }

  ngOnInit() {
    this.customerGroups$ = merge(
      this.customerGroupService.getAll(),
      this.customerGroupService.stateChanged.pipe(
        map(state => {
          if (state) {
            return state.customerGroups;
          }
        })
      )
    ) 
  }

  addGroup() {
    this.customerGroupService.add(null).subscribe();
  }

}
