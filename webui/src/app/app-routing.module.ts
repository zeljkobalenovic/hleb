import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CustomersComponent } from './customers/customers.component';
import { ProductsComponent } from './products/products.component';
import { OrdersComponent } from './orders/orders.component';
import { ReportsComponent } from './reports/reports.component';

const routes: Routes = [
  {path:'home' , component: HomeComponent},
  {path:'login' , component: LoginComponent},
  {path:'register' , component: RegisterComponent},
  {path:'customers' , component: CustomersComponent},
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
