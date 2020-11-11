import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerGroupContainerComponent } from './customer-group-container.component';

describe('CustomerGroupContainerComponent', () => {
  let component: CustomerGroupContainerComponent;
  let fixture: ComponentFixture<CustomerGroupContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerGroupContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerGroupContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
