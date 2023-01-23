import { Component, Input, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { encode } from 'src/external-dependencies/base64-arraybuffer-master/src';
import sha256 from 'src/external-dependencies/fast-sha256-js-1.1.0/sha256';
import { MessageService } from '../message.service';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})

/**
   * Manages the register user functionality.
   *
   * @remarks
   * This class handles the logic for 
   * registering a new user within the
   * program.
   */
export class RegisterUserComponent implements OnInit {
  @Input() login = false;
  currentUser: User | undefined
  invalidPassword : boolean = false;

  constructor(
    private userService: UserService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
  }

  setCurrentUser(user: User) : void {
    this.currentUser = user;
    this.messageService.add("Setting current user to " + this.currentUser?.username);
    this.userService.setCurrentUser?.(this.currentUser);
    this.talk.emit(true);
  }

  /**
   * Manages the user login functionality.
   *
   * @remarks
   * This method takes the username as a parameter
   * and calls the backend api to search for a user then if needed
   * add a new user
   * 
   * @param username the name the user logs onto the program with
   * @return void
   */
  loggedIn(username: string, password: string) {
    //hash password and convert to string
    // var nacl = require('tweetnacl');
    // nacl.util = require('tweetnacl-util');
    var hashArray : Uint8Array = sha256(new TextEncoder().encode(password));
    var hash : string = encode(hashArray);

    this.userService.getUserByName(username).subscribe(
      user => {
        if(user != null) {
          if(user.passwordHash != null && user.passwordHash == hash) { 
            this.invalidPassword = false;
            this.setCurrentUser(user);
          }
          else if(user.passwordHash == null) { //only for users who currently do not have a password
            user.passwordHash = hash;
            this.userService.updateUser(user).subscribe(
              updatedUser => {
                this.setCurrentUser(updatedUser);
              }
            );
            
          }
          else {
            this.invalidPassword = true;
          }
          
        }
        else {

          const myUser = {
            index: 0,
            username: username,
            status: false,
            cart: [],
            passwordHash: hash
          } as User; 

          console.log(myUser.passwordHash);

          this.userService.addUser(myUser).subscribe(
            user=> {
              this.setCurrentUser(user);
            }
          )
        }
      }
    )
  }
  
  //the purpose of this line is to inform the app-component that a user had logged in successfully
  //The app-component then retrieves the current user from the user service and starts the
  //website with that users information. 
  @Output() talk: EventEmitter<boolean> = new EventEmitter<boolean>();
}
