import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Menu } from '../entities/entities';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  baseUrl="Menu"

  constructor(private httpClient:HttpClient) { }

  public getMenusByLocalId(id:number){
    return this.httpClient.get<Menu[]>(this.baseUrl +"/api/Menu/GetMenusByLocalId/"+id)

  }

  public getMenuDetailById(menuId:number){
    return this.httpClient.get<Menu>(this.baseUrl +"/api/Menu/GetMenuById/"+menuId)
  }
}
