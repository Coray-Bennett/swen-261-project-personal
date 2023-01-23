import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, tap } from 'rxjs';
import { Discount } from './discount';
import { MessageService } from './message.service';
@Injectable({
  providedIn: 'root'
})
export class DiscountService {

  private discountsUrl = 'http://localhost:8080/discounts';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
  };
  
  constructor(private http: HttpClient,
    private messageService: MessageService) { }

  private log(message: string) {
    this.messageService.add(`DiscountService: ${message}`)
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
   * GET all discounts in from API
   * @returns array of discounts
   */
   getDiscounts(): Observable<Discount[]> {
    return this.http.get<Discount[]>(this.discountsUrl).pipe(
      tap(_ => this.log('got list of all discounts')),
      catchError(this.handleError<Discount[]>('getDiscounts', []))
    );
  }

  /**
   * PUT - updates information of passed discount
   * @param discount discount to be updated
   * @returns updated discount
   */
  updateDiscount(discount: Discount): Observable<any> {
    return this.http.put(this.discountsUrl, discount, this.httpOptions).pipe(
      tap(_ => this.log(`updated discount with index=${discount.index}`)),
      catchError(this.handleError<any>('updateDiscount'))
    );
  }
  
  /**
   * GET - gets a specific discount 
   * @param index - the index of the discount to be searched for 
   * @returns the discount with the matching index or nothing if a discount was not found 
   */
  getDiscount(index: number): Observable<Discount> {
    const url = `${this.discountsUrl}/${index}`;
    return this.http.get<Discount>(url).pipe(
      tap(_ => this.log(`got discount with index=${index}`)),
      catchError(this.handleError<Discount>(`getDiscount index=${index}`))
    );
  }


  /** POST: add a new discount to the server
   * @param discount : the new discount to be added to the server
   */
  addDiscount(discount: Discount): Observable<Discount> {
    return this.http.post<Discount>(this.discountsUrl, discount, this.httpOptions).pipe(
      tap((newDiscount: Discount) => this.log(`added discount w/ id=${newDiscount.index}`)),
      catchError(this.handleError<Discount>('addDiscount'))
    );
  }

    /** DELETE: delete the discount from the server */
    deleteDiscount(index: number): Observable<Discount> {
      const url = `${this.discountsUrl}/${index}`;
  
      return this.http.delete<Discount>(url, this.httpOptions).pipe(
        tap(_ => this.log(`deleted discount with index=${index}`)),
        catchError(this.handleError<Discount>('deleteJersey'))
      );
    }


 

  
  
}
