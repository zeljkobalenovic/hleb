import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerGroupEditComponent } from './customer-group-edit.component';

describe('CustomerGroupEditComponent', () => {
  let component: CustomerGroupEditComponent;
  let fixture: ComponentFixture<CustomerGroupEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerGroupEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerGroupEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
