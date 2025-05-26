import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { BASEURL } from 'src/environments/environment';
import { Local } from '../entities/entities';

@Injectable({
  providedIn: 'root'
})
export class LocalsService {


  baseUrl :string = "Local";
  constructor(private httpClient:HttpClient) { }

  public getLocalsByAddress(address:string){
   return  this.httpClient.get<Local[]>(this.baseUrl + "/api/Local/GetLocalsByCity/" + address)
  }

  public getLocalById(localId:number){
    return this.httpClient.get<Local>(this.baseUrl + "/api/Local/GetLocalById/"+localId);
  }
}
