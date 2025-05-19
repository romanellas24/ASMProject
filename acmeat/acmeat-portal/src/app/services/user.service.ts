import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BASEURL } from 'src/environments/environment';
import { UserInfo } from '../entities/entities';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  baseUrl:string = BASEURL.urlString;
  constructor(private httpClient:HttpClient) { }

  public login(username:string,pwd:string){
    this.httpClient.post("todo",{username:username,pwd:pwd})
  }

  public createUser(user:UserInfo){
    this.httpClient.post(this.baseUrl + "/User/api/User/CreateUser",user)

  }
}
