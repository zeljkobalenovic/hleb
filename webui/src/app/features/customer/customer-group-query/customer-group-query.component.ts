import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { of } from 'rxjs';
import { QueryOptions } from 'src/app/shared/store/store-state';

@Component({
  selector: 'app-customer-group-query',
  templateUrl: './customer-group-query.component.html',
  styleUrls: ['./customer-group-query.component.css']
})
export class CustomerGroupQueryComponent implements OnInit {

  private _customerGroupsQueryOptions : QueryOptions;

  @Input()
  get customerGroupsQueryOptions() : QueryOptions {
    return this._customerGroupsQueryOptions;
  }

  set customerGroupsQueryOptions( value : QueryOptions) {
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
