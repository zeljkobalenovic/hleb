import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerGroup } from 'src/app/core/model/customerGroup';

@Component({
  selector: 'app-customer-group-list',
  templateUrl: './customer-group-list.component.html',
  styleUrls: ['./customer-group-list.component.css']
})
export class CustomerGroupListComponent implements OnInit {

  private _customerGroups : CustomerGroup[] = [];

  @Input()
  get customerGroups() : CustomerGroup[] {
    return this._customerGroups;
  }

  set customerGroups( value : CustomerGroup[]) {
    if (value) {
      this._customerGroups=value;
    }
  }

  constructor() { }

  ngOnInit() {
  }

  customerTrackBy(index: number, customerGroup: CustomerGroup) {
    return customerGroup.id;
}

}
