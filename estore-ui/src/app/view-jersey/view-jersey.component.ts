import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { Discount } from '../discount';
import { JerseyService } from '../jersey.service';
import { UserService } from '../user.service';
import { User } from '../user'
import { DiscountedJersey } from '../DiscountedJersey';
 
@Component({
  selector: 'app-view-jersey',
  templateUrl: './view-jersey.component.html',
  styleUrls: ['./view-jersey.component.css']
})
 
/**
   * Manages displaying of products functionality.
   *
   * @remarks
   * This class manages how the products are display in
   * inventory.
   *
   * @return void
   */
export class ViewJerseyComponent implements OnInit, AfterViewInit {


  @Input() discountJersey ?: DiscountedJersey | null;
  discount ?: Discount;
  isAdmin : boolean | undefined = false;
  index : number = -1;
  currentUsername: string | undefined = ""
  remainingQuantity : number = 0
  inCart : boolean = false;
  unableToAdd : boolean = false;
  confirm : boolean = false;

  user ?: User;
 
  constructor(private jerseyService: JerseyService,
    private userService: UserService) { }
 
 
  ngOnInit(): void {

    this.user = this.userService.getCurrentUser();

    this.checkCart();
     
  }

  ngAfterViewInit(): void {

    this.user = this.userService.getCurrentUser();

    this.checkCart();

    this.currentUsername = this.userService.getCurrentUser()?.username;
    
    this.isAdmin = this.userService.getCurrentUser()?.status;

    if(this.discountJersey != null){
      this.index = this.discountJersey?.jersey.index;
    }
   
  }

  /**
   * Checks if the jersey is in the cart.
   *
   * @remarks
   * Checks if the user has jersey in their cart,
   * and if so, sets remining quantity to accurately
   * reflect it.
   *
   * @return void
   */
   checkCart(): void {
    if(this.user == null || this.discountJersey?.jersey == null) {
      return;
    }

    for(var i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i][0] === this.discountJersey.jersey.index) {
        this.remainingQuantity = this.discountJersey.jersey.quantity - this.user.cart[i][1];
        this.inCart = true;
        return;
      }
      else{
        this.inCart = false;
      }
    }
  }
 
  /**
   * Manages adding to cart functionality.
   *
   * @remarks
   * This method lets the user add products to their cart.
   *
   * @param string number of product to add to cart
   * @return void
   */
  addToCart(quantity : string) : void {
 
    if(this.user == null || this.discountJersey?.jersey == null) {
      return;
    }
 
    var added = false;
 
    for(var i = 0; i < this.user.cart.length; i++) {
      if(this.user.cart[i][0] === this.discountJersey.jersey.index) {
        var tempQuantity = this.user.cart[i][1] + Number(quantity);
        if(this.discountJersey.jersey.quantity - tempQuantity >= 0){
          this.user.cart[i][1] = tempQuantity;
        }
        else{
          this.unableToAdd = true;
        }
        
        this.remainingQuantity = this.discountJersey.jersey.quantity - this.user.cart[i][1];
        added = true;
        break;
      }
    }
 
    if(!added) {
      this.user.cart.push([this.discountJersey.jersey.index, Number(quantity)]);
      this.remainingQuantity = this.discountJersey.jersey.quantity - Number(quantity);
    }
    this.checkCart();
    this.updateUser();
  }
 
  /**
   * Updates the Api with new user contents.
   *
   * @remarks
   * Every time after the user is changed in some way,
   *  this method is called to updated the backend api
   *  with proper modified user.
   *
   * @return void
   */
  updateUser() : void {
 
    if(this.user != null) {
      this.userService.updateUser(this.user).subscribe();
    }
 
  }
 
  /**
   * This method gets the index of jersey to be deleted and passes
   * it to jersey service to be deleted in via backend api.
   * @return void
   */
   delete(): void {
    if(this.discountJersey?.jersey != null) {
      this.jerseyService.deleteJersey(this.discountJersey?.jersey.index).subscribe();
    }
 
    this.discountJersey = null;
  }

  confirmDelete() : void {
    this.confirm = true;
  }

  cancelDelete() : void {
    this.confirm = false;
  }
}
