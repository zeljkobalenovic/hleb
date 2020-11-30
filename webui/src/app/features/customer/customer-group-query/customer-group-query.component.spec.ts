import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerGroupQueryComponent } from './customer-group-query.component';

describe('CustomerGroupQueryComponent', () => {
  let component: CustomerGroupQueryComponent;
  let fixture: ComponentFixture<CustomerGroupQueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerGroupQueryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerGroupQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
