import { DiscountedJersey } from "./DiscountedJersey";

export class UserCart {
    discountedJersey : DiscountedJersey
    quantity : Number

    constructor(jerseyItem : DiscountedJersey, quantity : Number){
        this.discountedJersey = jerseyItem;
        this.quantity = quantity;
    }
}