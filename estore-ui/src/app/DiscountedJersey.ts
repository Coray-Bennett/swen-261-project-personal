import { Discount } from "./discount";
import { Jersey } from "./jersey";

export class DiscountedJersey {

    jersey : Jersey
    discount : Discount
    finalPrice : number

    constructor(jersey : Jersey, discount : Discount){
        this.jersey = jersey;
        this.discount = discount;
        this.finalPrice = jersey.price;
        this.calculatePrice();
    }

    calculatePrice() : void {
        if(this.discount == null) {
            return;
        }

        this.finalPrice *= (1 - (this.discount.percent / 100));
        this.finalPrice = Number(this.finalPrice.toFixed(2));
    }
}