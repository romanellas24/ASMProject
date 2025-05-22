import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GeneralResponse, Order } from '../entities/entities';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient:HttpClient) { }

  baseUrl:string="/Order"

  public createOrder(order:Order){
    return this.httpClient.post<GeneralResponse>(this.baseUrl+"/api/Order/CreateOrder",order)
  }
}
