import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { Discount } from '../discount';
import { DiscountService } from '../discount.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-create-discount',
  templateUrl: './create-discount.component.html',
  styleUrls: ['./create-discount.component.css']
})
/**
   * Manages the creation of discounts within the program.
   *
   * @remarks
   * This class is uesed by the admin to create new
   * discounts on products and set expiration dates
   * for discounts.
   */
export class CreateDiscountComponent implements OnInit {
  @Input() index ?: number;
  discount ?: Discount;
  discounts : Discount[] = [];
  date : string = "";
  confirm : boolean = false;

  constructor(private discountService: DiscountService,
    private messageService: MessageService) {
    }

  ngOnInit(): void {
    this.getDiscount();
  }

  ngAfterViewInit(): void {
    document.getElementById("new-discount-expiration")?.setAttribute("min", this.getCurrentDate());
  }

  /**
   * Gets the current date.
   *
   * @remarks
   * This method gets the current date to determine
   * what timeframe new discounts should be set to
   * expire at.
   * 
   * @return string - the string represenation of the date
   */
  getCurrentDate(): string {
    const d = new Date();
    return d.toISOString().substring(0, 16);
  }

  getDiscountDate(): string {
    if(!this.discount) {return this.getCurrentDate();}
    const d = new Date(this.discount.expiration);
    return d.toISOString().substring(0, 16);
  }

  getDiscount() : void {
    if(this.index != null) {
      this.discountService.getDiscount(this.index).subscribe(
        discount => {
          this.discount = discount;
          this.date = this.getDiscountDate();
        }
      )
    }
   
  }

  /**
   * Adds the discount.
   *
   * @remarks
   * This method adds the newly created discount to the
   * backend api.
   * 
   * @param  dIndex       unique index of discount which corresponds to jersey index
   * @param  dPercent     percent of product price that will be discounted
   * @param  dExpiration  expiration date for the discount
   * @return void
   */
  addDiscount(dIndex: number | undefined, dPercent: string, dExpiration: string): void {
    if(!dIndex || !dPercent || !dExpiration) {return;}

    const newDiscount = {
      index: Number(dIndex),
      percent: Number(dPercent),
      expiration: dExpiration
    } as Discount;

    this.discountService.addDiscount(newDiscount)
      .subscribe(discount => {
        this.discounts.push(discount);
        this.getDiscount();
      });
    
  }

  confirmDelete() : void {
    this.confirm = true;
  }

  cancelDelete() : void {
    this.confirm = false;
  }

  /**
   * Deletes the discount from the backend
   */
  deleteDiscount() : void {
    if(this.index == null) {
      return;
    }
    this.discountService.deleteDiscount(this.index).subscribe();
    this.discount = undefined;
  }
}
