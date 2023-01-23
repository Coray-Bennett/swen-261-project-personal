import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, tap } from 'rxjs';
import { MessageService } from './message.service';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})

export class UserService {
  user: User | undefined

  private userUrl = 'http://localhost:8080/users';
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
  }

  /** Used to access the messageservice to create messages after commands are used */
  private log(message: string) {
    this.messageService.add(`UserService: ${message}`)
  }

  /** 
   * Handles http operations that fail without stopping the app
   * @param operation - the name of the http operation that failed
   * @param result - optional value that can be returned as an observable object
  */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);

      this.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    };
  }

  setCurrentUser(user: User | undefined) {
    this.user = user
  }

  getCurrentUser(): User | undefined {
    return this.user
  }

  logout() {
    this.user = undefined
  }

  getUser(index: number): Observable<User> {
    const url = `${this.userUrl}/${index}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`got jersey with index=${index}`)),
      catchError(this.handleError<User>(`getJersey index=${index}`))
    );
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userUrl).pipe(
      tap(_ => this.log('got list of all jerseys')),
      catchError(this.handleError<User[]>('getJerseys', []))
    );
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.userUrl, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added user with index=${newUser.index}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  searchUsers(term: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.userUrl}/?key=${term}`).pipe(
      tap(x => x.length ? 
        this.log(`found user that matched "${term}"`):
        this.log(`no user matched "${term}"`)),
      catchError(this.handleError<User[]>('SearchUsers', []))
    )
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(this.userUrl, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user with index=${user.index}`)),
      catchError(this.handleError<any>('updateUser'))
    );
  }
  

  getUserByName(username: String): Observable<User> {
    const url = `${this.userUrl}/username/${username}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`got jersey with username=${username}`)),
      catchError(this.handleError<User>(`getJersey username=${username}`))
    );
  }
}