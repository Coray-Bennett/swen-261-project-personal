import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Jersey } from './jersey';
import { JERSEYS } from './mock-jerseys';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})

export class JerseyService {

  private jerseysUrl = 'http://localhost:8080/jerseys';
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
  };
  
  /** Used to access the messageservice to create messages after commands are used */
  private log(message: string) {
    this.messageService.add(`JerseyService: ${message}`)
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
  
  /**
   * GET all jerseys in from API
   * @returns array of jerseys
   */
  getJerseys(): Observable<Jersey[]> {
    return this.http.get<Jersey[]>(this.jerseysUrl).pipe(
      tap(_ => this.log('got list of all jerseys')),
      catchError(this.handleError<Jersey[]>('getJerseys', []))
    );
  }

  /**
   * PUT - updates information of passed jersey
   * @param jersey jersey to be updated
   * @returns updated jersey
   */
  updateJersey(jersey: Jersey): Observable<any> {
    return this.http.put(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap(_ => this.log(`updated jersey with index=${jersey.index}`)),
      catchError(this.handleError<any>('updateJersey'))
    );
  }
  
  /**
   * GET - gets a specific jersey 
   * @param index - the index of the jersey to be searched for 
   * @returns the jersey with the matching index or nothing if a jersey was not found 
   */
  getJersey(index: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${index}`;
    return this.http.get<Jersey>(url).pipe(
      tap(_ => this.log(`got jersey with index=${index}`)),
      catchError(this.handleError<Jersey>(`getJersey index=${index}`))
    );
  }

  /**
   * GET - returns only jersey that match the search term
   * @param term the search term or terms entered by the user
   * @returns a list of all jerseys that match the search term
   */
  searchJerseys(term: string): Observable<Jersey[]> {
    return this.http.get<Jersey[]>(`${this.jerseysUrl}/?key=${term}`).pipe(
      tap(x => x.length ? 
        this.log(`found jersey that matched "${term}"`):
        this.log(`no jerseys matched "${term}"`)),
      catchError(this.handleError<Jersey[]>('SearchJerseys', []))
    )
  }

  /** POST: add a new jersey to the server */
  addJersey(jersey: Jersey): Observable<Jersey> {
    return this.http.post<Jersey>(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap((newJersey: Jersey) => this.log(`added jersey with index=${newJersey.index}`)),
      catchError(this.handleError<Jersey>('addJersey'))
    );
  }

  /** DELETE: delete the jersey from the server */
  deleteJersey(index: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${index}`;

    return this.http.delete<Jersey>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted jersey with index=${index}`)),
      catchError(this.handleError<Jersey>('deleteJersey'))
    );
  }
}

