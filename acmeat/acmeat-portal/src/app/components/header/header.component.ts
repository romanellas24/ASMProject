import { Component, OnInit } from '@angular/core';
import { Observable, take } from 'rxjs';
import { OrderInfo, UserInfo } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {


  isAuthenticated$ : Observable<boolean> = new Observable();
  ordersToPay$ : Observable<OrderInfo[]> = new Observable();
  constructor(
    private eventsService:EventsService,
    private userSvc: UserService,
    private orderSvc: OrderService
  
  ) { }

  ngOnInit(): void {
    if(this.userSvc.getUserInfo() != undefined){
      this.userSvc.setUserAuthenticated(true);
    }else{
      this.userSvc.setUserAuthenticated(false);
    }


    this.isAuthenticated$ = this.userSvc.isAuthenticated$

    let user : UserInfo | undefined = this.userSvc.getUserInfo();
    if(user!=undefined)
      this.ordersToPay$ = this.orderSvc.getOrdersToPay(user.id).pipe(take(1));
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

  getMail(){
    
    let user: UserInfo | undefined = this.userSvc.getUserInfo();
    
    if(user != undefined){
      return user?.mail;
    }else{
      return "Accedi";
    }
    
    

  }


  openCartMenu(){
    this.eventsService.isCartMenuEnabled$.next(true);
    this.eventsService.isSideMenuEnabled$.next(true);
  }

}
