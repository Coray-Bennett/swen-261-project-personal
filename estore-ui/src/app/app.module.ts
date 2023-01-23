import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { EstoreRoutingModule } from './estoreRouting.module';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { UpdateJerseyComponent } from './update-jersey/update-jersey.component';
import { JerseySearchComponent } from './jersey-search/jersey-search.component';
import { CreateJerseyComponent } from './create-jersey/create-jersey.component';
import { MessagesComponent } from './messages/messages.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { ViewJerseyComponent } from './view-jersey/view-jersey.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { CreateDiscountComponent } from './create-discount/create-discount.component';

@NgModule({
  declarations: [
    AppComponent,
    UpdateJerseyComponent,
    JerseySearchComponent,
    CreateJerseyComponent,
    MessagesComponent,
    RegisterUserComponent,
    ViewJerseyComponent,
    ShoppingCartComponent,
    CheckoutComponent,
    CreateDiscountComponent
  ],
  imports: [
    BrowserModule,
    EstoreRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
