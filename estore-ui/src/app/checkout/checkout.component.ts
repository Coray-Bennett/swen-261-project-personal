import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JerseyService } from '../jersey.service';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})

/**
   * Manages the checkout functionality.
   *
   * @remarks
   * This class handles the logic for the
   * shipping and payment page.
   */
export class CheckoutComponent implements OnInit {

  user?: User;
  purchasable: boolean = true;
  constructor(private jerseyService: JerseyService, private userService: UserService, public router: Router) { }

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser();
  }

  /**
   * Manages the clearing of shopping cart functionality.
   *
   * @remarks
   * Once the user purchases items, this method decrements
   * the jerseys quantities in inventory, deletes any 
   * jersey that have a quantity of zero, and sets
   * the users shopping cart to empty. Error checking
   * for purchasable quantity from inventory is done 
   * in view jersey component.
   *
   * @return void
   */
   clearCart(): void {
    if(this.user?.cart == null) {
      return;
    }
   
    for(var i = 0; this.user?.cart != undefined && i < this.user.cart.length; i++) {
     
      var index = this.user.cart[i][0];
 
      this.jerseyService.getJersey(Number(index)).subscribe(
        jersey => {
          if(this.user){
            for(var j = 0; this.user?.cart != undefined && j < this.user.cart.length; j++) {
              console.log("c")
              if(jersey.index === this.user.cart[j][0]){
                jersey.quantity = Number(jersey.quantity - this.user.cart[j][1])
                if(jersey.quantity === 0){
                  this.jerseyService.deleteJersey(jersey.index).subscribe();
                }
                else{
                  this.jerseyService.updateJersey(jersey).subscribe();
                }
                this.user.cart[j] = []
                this.updateUser();
                this.router.navigateByUrl('/', {skipLocationChange:true}).then(()=>{
                this.router.navigate([`/shopping-cart`])})
                            
                break;
              }
            }
          }
      });
    }
    this.updateUser();
    this.router.navigateByUrl('/', {skipLocationChange:true}).then(()=>{
    this.router.navigate([`/shopping-cart`])})
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
}
