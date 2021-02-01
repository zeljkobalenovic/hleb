import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-customer-query',
  templateUrl: './customer-query.component.html',
  styleUrls: ['./customer-query.component.css']
})
export class CustomerQueryComponent implements OnInit {

  pageSizes = [5, 10, 25];
  selectedButton = 10;

  constructor() { }

  ngOnInit() {
  }

  setPageSize(pageSize: number) {
    
  }

}
