import { Component, Input, OnInit } from '@angular/core';
import { JerseyService } from '../jersey.service';
import { Jersey } from '../jersey';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Discount } from '../discount';
import { UserService } from '../user.service';

@Component({
  selector: 'app-update-jersey',
  templateUrl: './update-jersey.component.html',
  styleUrls: ['./update-jersey.component.css']
})

export class UpdateJerseyComponent implements OnInit {
  @Input() jersey?: Jersey;
  discount ?: Discount;
  isAdmin : boolean | undefined = false;

  constructor(
    private jerseyService: JerseyService,
    private userService: UserService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.getCurrentUser()?.status;
    this.getJersey();
  }

  /**
   * uses the jersey service to get a jersey from its index
   */
  getJersey(): void {
    const index = Number(this.route.snapshot.paramMap.get('index'));
    this.jerseyService.getJersey(index)
      .subscribe(jersey => this.jersey = jersey);
  }

  /**
   * goes back to the jersey list component
   */
  goBack(): void {
    this.location.back();
  }

  /**
   * uses the jersey service to call update the jersey with the API
   */
  updateJersey(): void {
    if (this.jersey) {
      this.jerseyService.updateJersey(this.jersey)
        .subscribe();
    }
  }


  

}