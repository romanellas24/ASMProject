import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BASEURL } from 'src/environments/environment';
import { GeneralResponse, UserInfo } from '../entities/entities';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  isAuthenticated$:BehaviorSubject<boolean> = new BehaviorSubject(false);

  baseUrl:string ="/User";
  constructor(private httpClient:HttpClient) { }

  public login(email:string,pwd:string):Observable<UserInfo>{
    return this.httpClient.post<UserInfo>(this.baseUrl + "/api/User/Login",{mail:email,pwd:pwd})
  }

  public createUser(user:UserInfo) :Observable<GeneralResponse>{
    return this.httpClient.post<GeneralResponse>(this.baseUrl + "/api/User/CreateUser",user)

  }

  public setUserAuthenticated(value:boolean){
    this.isAuthenticated$.next(value)
  }
}
