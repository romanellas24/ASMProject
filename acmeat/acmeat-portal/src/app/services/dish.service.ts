import { Injectable } from '@angular/core';
import { Dish } from '../entities/entities';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DishService {

   baseUrl :string = "Dish";
    constructor(private httpClient:HttpClient) { }
  
  
    public getDishById(DishId:number){
      return this.httpClient.get<Dish>(this.baseUrl + "/api/Dish/GetDishById/"+DishId);
    }


    public getDishsByMenuId(MenuId:number){
      return this.httpClient.get<Dish[]>(this.baseUrl + "/api/Dish/GetDishsByMenuId/"+MenuId);
    }
}
