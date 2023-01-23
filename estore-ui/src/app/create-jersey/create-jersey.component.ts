import { Component, OnInit } from '@angular/core';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-create-jersey',
  templateUrl: './create-jersey.component.html',
  styleUrls: ['./create-jersey.component.css']
})

/**
   * Manages the create product functionality.
   *
   * @remarks
   * This class deals with creating a new jersey object 
   * from user input data and passing it to the backend 
   * api.
   */
export class CreateJerseyComponent implements OnInit {

  jerseys: Jersey[] = [];
  
  constructor(private jerseyService: JerseyService) { }

  ngOnInit(): void {
  }

  /**
   * Manages the add product functionality.
   *
   * @remarks
   * This method always creates a jersey class object from provided 
   * input data and passes it to jerseyservice which interacts with
   * the backend api.
   * 
   * @param  jName  name on jersey
   * @param  jNumber number on jersey
   * @param  jPrice price of jersey
   * @param  jSize size of jersey
   * @param  jColor color of jersey
   * @param  jQuantity quantity of jersey
   * @return void
   */
  add(jName: string, jNumber: string, jPrice: string, jSize: string, jColor: string, jQuantity: string): void {
    jName = jName.trim();
    jNumber = jNumber.trim();
    jPrice = jPrice.trim();
    jSize = jSize.trim();
    jColor = jColor.trim();
    jQuantity = jQuantity.trim();

    if (!jName || !jNumber || !jPrice || !jSize || !jColor || !jQuantity) { return; }

    const myJersey = {
      index: 0,
      name: jName,
      number: Number(jNumber),
      price: Number(jPrice),
      size: jSize,
      color: jColor,
      quantity: Number(jQuantity)
    } as Jersey;
  
    this.jerseyService.addJersey(myJersey)
      .subscribe(jersey => {
        this.jerseys.push(jersey);
      });
  }
}
