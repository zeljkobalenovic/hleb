import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

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
import { CustomerContainerComponent } from './features/customer/customer-container.component';
import { CustomerGroupListComponent } from './features/customer/customer-group-list/customer-group-list.component';
import { CustomerGroupEditComponent } from './features/customer/customer-group-edit/customer-group-edit.component';
import { CustomerEditComponent } from './features/customer/customer-edit/customer-edit.component';
import { CustomerListComponent } from './features/customer/customer-list/customer-list.component';
import { CustomerGroupQueryComponent } from './features/customer/customer-group-query/customer-group-query.component';
import { GlobalErrorHandler } from './core/error/globalErrorHandler';
import { BackendErrorInterceptor } from './core/interceptors/backendErrorInterceptor';
import { ModalNotificationComponent } from './shared/components/modal-notification.component';
import { AuthInterceptor } from './core/interceptors/authInterceptor';
import { LoaderInterceptor } from './core/interceptors/loaderInterceptor';


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
    CustomerContainerComponent,
    CustomerGroupListComponent,
    CustomerGroupEditComponent,
    CustomerEditComponent,
    CustomerListComponent,
    CustomerGroupQueryComponent,
    ModalNotificationComponent,  
    
    
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
  entryComponents: [
    ModalNotificationComponent
  ],
  providers: [
    {provide:ErrorHandler,useClass:GlobalErrorHandler},
    {provide:HTTP_INTERCEPTORS,useClass:LoaderInterceptor,multi:true},
    {provide:HTTP_INTERCEPTORS,useClass:AuthInterceptor,multi:true},
    {provide:HTTP_INTERCEPTORS,useClass:BackendErrorInterceptor,multi:true},
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
