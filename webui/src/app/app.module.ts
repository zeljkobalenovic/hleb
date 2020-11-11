import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { CustomersComponent } from './entities/customers/customers.component';
import { ProductsComponent } from './entities/products/products.component';
import { OrdersComponent } from './entities/orders/orders.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ReportsComponent } from './entities/reports/reports.component';
import { HasAnyAuthorityDirective } from './shared/auth/has-any-authority.directive';
import { CustomerGroupContainerComponent } from './features/customerGroup/customer-group-container.component';



@NgModule({
  declarations: [
    AppComponent,
    NavMenuComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    CustomersComponent,
    ProductsComponent,
    OrdersComponent,
    ReportsComponent,
    HasAnyAuthorityDirective,
    CustomerGroupContainerComponent,
    
    
    
    
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    // mora ovako tj. mora biti dostupno SVIMA !!! (znaci u root) jer iskace nezavisno od komponenti i mora se tako hendlati 
    ModalModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
