import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JerseySearchComponent } from './jersey-search/jersey-search.component';
import { CreateJerseyComponent } from './create-jersey/create-jersey.component';
import { UpdateJerseyComponent } from './update-jersey/update-jersey.component';
import { CreateDiscountComponent } from './create-discount/create-discount.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { ViewJerseyComponent } from './view-jersey/view-jersey.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CheckoutComponent } from './checkout/checkout.component';


const routes: Routes = [
  { path: 'update-jersey/:index', component: UpdateJerseyComponent},
  { path: 'jersey-search', component: JerseySearchComponent},
  { path: 'create-jersey', component: CreateJerseyComponent},
  { path: 'create-discount', component: CreateDiscountComponent},
  { path: 'view-jersey', component: ViewJerseyComponent},
  { path: 'register-user', component: RegisterUserComponent},
  { path: 'shopping-cart', component: ShoppingCartComponent},
  { path: 'checkout', component: CheckoutComponent}
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class EstoreRoutingModule { }
