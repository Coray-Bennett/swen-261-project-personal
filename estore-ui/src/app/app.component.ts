import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from './message.service';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'BRUINS JERSEYS';
  currentUser: User | undefined
  adminOnline: boolean | undefined
  online: boolean = false

  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.router.navigateByUrl('/')
  }

  login(event: boolean) {
    this.currentUser = this.userService.getCurrentUser()
    this.adminOnline = this.currentUser?.status
    this.online = true

    this.router.navigateByUrl('/jersey-search')
  }

  logout(){
    this.userService.logout()
    this.online = false
    this.adminOnline = false
  }
}
