import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CustomersComponent } from './entities/customers/customers.component';
import { ProductsComponent } from './entities/products/products.component';
import { OrdersComponent } from './entities/orders/orders.component';
import { ReportsComponent } from './entities/reports/reports.component';
import { CustomerContainerComponent } from './features/customer/customer-container.component';
import { CustomerGroupListComponent } from './features/customer/customer-group-list/customer-group-list.component';
import { CustomerGroupEditComponent } from './features/customer/customer-group-edit/customer-group-edit.component';


const routes: Routes = [
  {path:'home' , component: HomeComponent},
  {path:'login' , component: LoginComponent},
  {path:'register' , component: RegisterComponent},
 // {path:'customers' , component: CustomersComponent},
  {path:'customers' , component: CustomerContainerComponent},
  {path:'customergroups' , component:CustomerGroupListComponent},
  {path:'customergroupsedit', component:CustomerGroupEditComponent},
  {path:'customergroupsedit/:id', component:CustomerGroupEditComponent},
  {path:'products' , component: ProductsComponent},
  {path:'orders' , component: OrdersComponent},
  {path:'reports' , component: ReportsComponent},
  {path: '' , component:HomeComponent , pathMatch: 'full'},
  {path: '**' , redirectTo:'/home'}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
