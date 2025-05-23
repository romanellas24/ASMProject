import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { BehaviorSubject, map, mergeMap, Observable, switchMap, take, tap, withLatestFrom } from 'rxjs';
import { GeneralResponse, Local, Menu, MenuType, OrderInfo, UserInfo } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { LocalsService } from 'src/app/services/locals.service';
import { MenuService } from 'src/app/services/menu.service';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss']
})
export class SidemenuComponent implements OnInit {

  orderList$ : Observable<OrderInfo[]> = new Observable();
  isAuthenticated$ : Observable<boolean> = new Observable();
  isCartMenuEnabled$ : Observable<boolean> = new Observable();

  localList$ = [];
    menList$  = [];
  

  constructor(
    private eventsService:EventsService,
    private orderSvc: OrderService,
    private localSvc : LocalsService,
    private menuSvc : MenuService,
    private router: Router,
    private userSvc:UserService) { }

  ngOnInit(): void {
    this.isAuthenticated$ = this.userSvc.isAuthenticated$
    this.isCartMenuEnabled$ = this.eventsService.isCartMenuEnabled$
    let user: UserInfo | undefined = this.userSvc.getUserInfo();

    // TO DO GET MENUS AND LOCALS WITH OBSERVABLES
    if(user != undefined){
      this.orderList$ = this.orderSvc.getOrdersToPay(user.id)
      
      
      

    }

   
      

  
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

  toggleCart():void{  
    let oldValue :boolean = this.eventsService.isCartMenuEnabled$.value;
    this.eventsService.isCartMenuEnabled$.next(!oldValue)
    this.toggleMenu()
  }

  getLocalInfo(localId:number):Observable<Local>{
    return this.localSvc.getLocalById(localId).pipe(take(1));
  }

  getMenuInfo(menuId:number):Observable<Menu>{
    return this.menuSvc.getMenuDetailById(menuId).pipe(take(1));
  }

  public isFish(menuType:string) {
        return menuType == MenuType.FISH
    }
  
    public isDinner(openingTime:string, closingTime:string){
    
  var startTime = moment(openingTime, "HH:mm");
  var endTime = moment(closingTime, "HH:mm");
  
      var dinnerStart= moment("19:00", "HH:mm");
      var dinnerEnds = moment("22:00","HH:mm");
     let amIBetween = dinnerStart <= startTime && dinnerEnds >= endTime 
     console.log(amIBetween);
     
     
     return amIBetween;//  returns false.  if date ignored I expect TRUE
    }

  async deleteOrder(orderId:number){
    let response :GeneralResponse | undefined= await this.orderSvc.deleteOrderById(orderId).toPromise()
    if(response?.message != "OK"){
      window.alert("There was an error while deleting order " + orderId +" Problem:" +response?.message)
    }
  }

  public goToManageOrders(){
    this.router.navigate(["orders"]);
  }

  public goToBank(){
    window.alert("TO DO")
  }

}
