import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Customer } from 'src/app/core/model/customer';
import { StoreState } from 'src/app/shared/store/store-state';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  private _customers : Customer[] = [];

  @Input()
  get customers() : Customer[] {
    return this._customers;
  }

  set customers( value : Customer[ ]) {
    if (value) {
      this._customers=value;
    }
  }

  @Output()
  changed : EventEmitter<string> = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
  }

  onClick(id:string) {
    this.changed.emit(id);
  
  }

}
