import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { BehaviorSubject, map, mergeMap, Observable, repeatWhen, Subject, Subscription, switchMap, take, tap, withLatestFrom } from 'rxjs';
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
export class SidemenuComponent implements OnInit,OnDestroy {

  orderList$ : Observable<OrderInfo[]> = new Observable();
  isAuthenticated$ : Observable<boolean> = new Observable();
  isCartMenuEnabled$ : Observable<boolean> = new Observable();
  local$:Observable<Local> = new Observable();
  menu$ : Observable<Menu> = new Observable();
  updatedSource$ :Subject<boolean> = new Subject(); 

  orderList:OrderInfo[]= []
  localList:Local[]=[]
  menuList:Menu[]=[]
  isLoading:boolean = false;

  subscriptionList:Subscription[] = []

   

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
      

      // TO DO REFACTOR WITH RXJS
      this.subscriptionList.push(
      this.orderList$.pipe(
        repeatWhen(()=>this.updatedSource$),
        tap(() => this.isLoading = true)
      ).subscribe((orders:OrderInfo[]) =>{
        // debugger
        this.orderList = orders

        this.orderList.forEach( order =>{
        this.local$ = this.localSvc.getLocalById(order.localId)
        this.menu$ = this.menuSvc.getMenuDetailById(order.menuId)

        this.subscriptionList.push(
        this.local$.pipe(
          tap((local:Local) => this.localList.push(local))
        ).subscribe()
      );


      this.subscriptionList.push(
        this.menu$.pipe(
          tap((menu:Menu) => this.menuList.push(menu)),
          tap(()=> this.isLoading =false)
        ).subscribe()
      );

      })
      } ));

      
     

    
      
      

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
    //  console.log(amIBetween);
     
     
     return amIBetween;//  returns false.  if date ignored I expect TRUE
    }

  async deleteOrder(orderId:number){
    this.isLoading=true
    let response :GeneralResponse | undefined= await this.orderSvc.deleteOrderById(orderId).toPromise()
    if(response?.message != "OK"){
      window.alert("There was an error while deleting order " + orderId +" Problem:" +response?.message)
    }else{
      window.alert("order deleted with success!")
    }
    this.updatedSource$.next(true)
    this.isLoading=false
  }

  Logout(){
    this.userSvc.Logout()
    window.location.reload()
  }

  public goToManageOrders(){
    this.router.navigate(["orders"]);
  }

  public goToBank(){
    window.alert("TO DO")
  }

  ngOnDestroy(): void {
   this.subscriptionList.forEach(
    sub => sub.unsubscribe()
   )
  }

}
