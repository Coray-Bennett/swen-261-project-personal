import {Component, OnInit } from '@angular/core';
import { distinctUntilChanged } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { debounceTime } from 'rxjs/internal/operators/debounceTime';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { Subject } from 'rxjs/internal/Subject';
import { Discount } from '../discount';
import { DiscountService } from '../discount.service';
import { DiscountedJersey } from '../DiscountedJersey';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { User } from '../user';
import { UserService } from '../user.service';
 
@Component({
  selector: 'app-jersey-search',
  templateUrl: './jersey-search.component.html',
  styleUrls: ['./jersey-search.component.css']
})
 
/**
   * Manages the search functionality.
   *
   * @remarks
   * This class handles the logic for
   * searching the inventory for a specific
   * product.
   */
export class JerseySearchComponent implements OnInit {
  jerseys: Jersey[] = [];
  discountedJerseys : DiscountedJersey[] = [];
  discounts : Discount[] = [];
  jerseys$!: Observable<Jersey[]>;
  users$!: Observable<User[]>
  users: User[] | undefined = [];
  private searchTerms = new Subject<string>();
  online: boolean = false;
 
  constructor(private discountService: DiscountService, private jerseyService: JerseyService, private userService: UserService) {
      this.getJerseys();
   }
 
  /**
   * Manages search functionality.
   *
   * @remarks
   * This method searches for jersey with specified parameter.
   *
   * @param term the data to search inventory for
   * @return void
   */
  search(term: string): void {
    this.searchTerms.next(term);
  }

  /**
   * Gets the jersey inventory from api.
   *
   * @remarks
   * This method calls the jersey service to get 
   * jersey inventory from the backend api.
   *
   * @return void
   */
  getJerseys(): void {
    this.jerseyService.getJerseys()
      .subscribe(jerseys => {
        this.jerseys = jerseys
        this.fillDiscountedJerseyArray();
      }
    );
  }

  /**
   * Creates a list of combined jerseys and discounts.
   *
   * @remarks
   * This method combines all the jerseys with their 
   * discount (if any exist), into single objects.
   * It then pushes them onto a new object list which 
   * represents the inventory.
   *
   * @return void
   */
  fillDiscountedJerseyArray(): void {
    for(let jersey of this.jerseys){
      this.discountService.getDiscount(jersey.index)
        .subscribe(discount => { 
          this.discountedJerseys.push(new DiscountedJersey(jersey, discount));
        });
      }
  }
 
  ngOnInit(): void {
    this.online = this.userService.getCurrentUser()?.username !== "";

    this.jerseys$ = this.searchTerms.pipe(
 
      //makes the program wait 300ms after a keystroke before searching for the search term
      debounceTime(300),

      //ignore new term if same as previous term
      distinctUntilChanged(),
 
      switchMap((term: string) => this.jerseyService.searchJerseys(term))
    )

    this.jerseys$.subscribe(jerseys => {
      this.discountedJerseys = [];
      this.jerseys = jerseys
      this.fillDiscountedJerseyArray();
    });
    
  }
 
  add(username: string): void {
    username = username.trim();
 
    if (!username) { return; }
 
    const myUser = {
      index: 0,
      username: username,
      status: false
    } as User;
 
    this.userService.addUser(myUser)
      .subscribe(user => {
        this.users?.push(user);
      });
  }
 
  /**
   * Manages sort functionality.
   *
   * @remarks
   * This method enables the user to sort the inventory
   * by predifined variations.
   * 
   * cases:
   *   default : default
   *   low to high : 1
   *   high to low : 2
   *   alphabetical : 3
   *   reverse alphabetical : 4
   *
   * @param input the id corresponding to the sort to enact
   * @return void
   */
   sortJerseys(input: number): void {
    //this.jerseys$.subscribe(jerseys => this.jerseys = jerseys);
    switch (input) {
      case 1: {
        let sortedJersey = this.discountedJerseys.sort((a, b) => (a.finalPrice <= b.finalPrice) ? -1 : 1);
        this.discountedJerseys = sortedJersey;
        break;
      }
      case 2: {
        let sortedJersey = this.discountedJerseys.sort((a, b) => (a.finalPrice >= b.finalPrice) ? -1 : 1);
        this.discountedJerseys = sortedJersey;
        }
        break;
      case 3: {
        let sortedJersey = this.discountedJerseys.sort((a, b) => (a.jersey.name < b.jersey.name) ? -1 : 1);
        this.discountedJerseys = sortedJersey;
        break;
      }
      case 4: {
        let sortedJersey = this.discountedJerseys.sort((a, b) => (a.jersey.name > b.jersey.name) ? -1 : 1);
        this.discountedJerseys = sortedJersey;
        break;
      }
      default: {
        break;
      }
    }
  }
}