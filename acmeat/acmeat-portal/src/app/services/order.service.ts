import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BankToken, GeneralResponse, OrderInfo } from '../entities/entities';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  ordersToPay$ : BehaviorSubject<OrderInfo[]> = new BehaviorSubject<OrderInfo[]>([]);

  constructor(private httpClient:HttpClient) { }

  baseUrl:string="/Order"

  public createOrder(orderInfo:OrderInfo){
    return this.httpClient.post<GeneralResponse>(this.baseUrl+"/api/Order/CreateOrder",orderInfo)
  }

  public getOrdersToPay(userId:number){
    return this.httpClient.get<OrderInfo[]>(this.baseUrl+"/api/Order/GetOrdersToPay/"+userId);
  }

  public getOrdersByUserId(userId:number){
    return this.httpClient.get<OrderInfo[]>(this.baseUrl+"/api/Order/GetOrdersByUserId/"+ userId)
  }

  public deleteOrderById(orderId:number){
    return this.httpClient.delete<GeneralResponse>(this.baseUrl+"/api/Order/DeleteOrderById/"+orderId);
  }

  public getPaymentToken(price:number){
    return this.httpClient.post<BankToken>("/payments",{
      amount:price,
      dest_account: 3
    });
  }
}
