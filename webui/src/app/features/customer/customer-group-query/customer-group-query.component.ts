import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { of } from 'rxjs';
import { customerGroupQueryOptions } from 'src/app/shared/store/store-state';

@Component({
  selector: 'app-customer-group-query',
  templateUrl: './customer-group-query.component.html',
  styleUrls: ['./customer-group-query.component.css']
})
export class CustomerGroupQueryComponent implements OnInit {

  private _customerGroupsQueryOptions : customerGroupQueryOptions;

  @Input()
  get customerGroupsQueryOptions() : customerGroupQueryOptions {
    return this._customerGroupsQueryOptions;
  }

  set customerGroupsQueryOptions( value : customerGroupQueryOptions) {
    if (value) {
      this._customerGroupsQueryOptions=value;
    }
  }

  @Output()
  changed : EventEmitter<string> = new EventEmitter<string>();


  constructor() { }

  ngOnInit() {
  }

  doFilter(searchString : string) {
    this.changed.emit(searchString);
  }

}
