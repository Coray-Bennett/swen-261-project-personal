import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { JerseyService } from '../jersey.service';
import { User } from '../user';
import { Router } from '@angular/router';
import { Discount } from '../discount';
import { DiscountService } from '../discount.service';
import { UserCart } from '../UserCart';
import { DiscountedJersey } from '../DiscountedJersey';
 
@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
 
/**
   * Manages the cart functionality.
   *
   * @remarks
   * This class handles the logic for
   * adding and displaying the users shopping
   * cart.
   */
export class ShoppingCartComponent implements OnInit {
  userCart: UserCart[] = []
  discount ?: Discount;
  user?: User;
  emptyCart!: boolean
 
  constructor(private userService: UserService, private jerseyService: JerseyService, private discountService: DiscountService, public router: Router) { }
 
  ngOnInit(): void {
    this.user = this.userService.getCurrentUser();
    this.getCart();
  }
 
  updateUser() : void {
    if(this.user != null) {
      this.userService.updateUser(this.user).subscribe();
    }
  }
 
  /**
   * Manages cart retrieval functionality.
   *
   * @remarks
   * This method takes the object of the current user logged in
   * and extracts the cart from it for displaying to the ui.
   *
   * @return void
   */
  getCart(): void {

    this.userCart = []
    if(this.user?.cart.length === 0) {
      this.emptyCart = true
      return;
    }
    this.emptyCart = false
    
    console.log(this.user?.cart)
 
    for(var i = 0; this.user?.cart != undefined && i < this.user.cart.length; i++) {
     
      var index = this.user.cart[i][0];
 
      this.jerseyService.getJersey(Number(index)).subscribe(
        jersey => {
          this.discountService.getDiscount(jersey.index)
              .subscribe(discount => {
                for(var i = 0; this.user?.cart != undefined && i < this.user.cart.length; i++) {
                  if(jersey.index === this.user?.cart[i][0]){
                    this.userCart[i] = (new UserCart(new DiscountedJersey(jersey, discount), this.user.cart[i][1]))
                  }
                }
          });
        });
      }
    }

    calcTotal() : string {
      let total = 0
      for(let jerseyItem of this.userCart){
        if(jerseyItem){
          total += Number(jerseyItem.discountedJersey.finalPrice * Number(jerseyItem.quantity));
        }
      }
      return total.toFixed(2);
    }
 
  /**
   * Manages product removal from cart functionality.
   *
   * @remarks
   * Removes item from shopping cart.
   *
   * @param index unique identifier of product to remove
   * @return void
   */
  removeItem(index: number): void {
    if(this.user?.cart == null) {
      return;
    }
 
    var newCart : number[][] = [];
 
    for(var i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i][0] !== index) {
        newCart.push(this.user.cart[i]);
      }
    }
      this.user.cart = newCart;
 
      this.updateUser();
      this.getCart();
      this.router.navigateByUrl('/', {skipLocationChange:true}).then(()=>{
      this.router.navigate([`/shopping-cart`])})
  }
 
  /**
   * Increments product quantity
   *
   * @remarks
   * Increments product quantity.
   *
   * @param index unique identifier of product to increment
   * @return void
   */
   increment(index: number): void {
    if(this.user?.cart == null) {
      return;
    }
 
    for(var i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i][0] === index) {
        this.user.cart[i][1] = this.user.cart[i][1] + 1;
      }
    }
 
    this.updateUser();
    this.getCart();
    this.router.navigateByUrl('/', {skipLocationChange:true}).then(()=>{
    this.router.navigate([`/shopping-cart`])})
  }
 
  /**
   * Decrements product quantity
   *
   * @remarks
   * Decrements quantity.
   *
   * @param index unique identifier of product to decrement
   * @return void
   */
  decrement(index: number): void {
    if(this.user?.cart == null) {
      return;
    }
 
    for(var i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i][0] === index) {
        if(this.user.cart[i][1] > 1){
          this.user.cart[i][1] = this.user.cart[i][1] - 1;
        }
        else{
          this.removeItem(index);
          return;
        }
      }
    }
   
    this.updateUser();
    this.getCart();
    this.router.navigateByUrl('/', {skipLocationChange:true}).then(()=>{
    this.router.navigate([`/shopping-cart`])})
  }
}
