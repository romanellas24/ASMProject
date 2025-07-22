
import { Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { end } from '@popperjs/core';
import { Observable, Subscription, tap } from 'rxjs';
import { GeneralResponse, Local, OrderInfo, UserInfo } from 'src/app/entities/entities';
import { LocalsService } from 'src/app/services/locals.service';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {

  orderList$: Observable<OrderInfo[]> = new Observable()
  local$: Observable<Local> = new Observable();
  subscriptionList: Subscription[] = [];
  localList: Local[] = []

  timeLeft$: Observable<string> = new Observable();

  constructor(
     @Inject(DOCUMENT) private document: Document,
    private userSvc: UserService,
    private orderSvc: OrderService,
    private localSvc: LocalsService
  ) { }

  ngOnInit(): void {

    if (this.userSvc.isAuthenticated$.value == true) {
      let user: UserInfo | undefined = this.userSvc.getUserInfo();

      if (user != undefined) {

        this.orderList$ = this.orderSvc.getOrdersByUserId(user.id)
        this.orderList$.subscribe((orders: OrderInfo[]) => {


          orders.forEach(order => {
            this.local$ = this.localSvc.getLocalById(order.localId)


            this.subscriptionList.push(
              this.local$.pipe(
                tap((local: Local) => this.localList.push(local))
              ).subscribe()
            );


          })
        });
      }

    }
  }


  async deleteOrder(orderId: number | undefined) {
    if(orderId != undefined){

         let response: GeneralResponse | undefined = await this.orderSvc.deleteOrderById(orderId).toPromise()
    if (response?.message != "OK") {
      window.alert("There was an error while deleting order " + orderId + " Problem:" + response?.message)
    }
    }
 
  }

  isPast(order: OrderInfo):boolean{
     let deliveryTime :Date = new Date(order.deliveryTime);
    let now: Date = new Date()
    // debugger
    
    if(deliveryTime < now){
      return true;
    }

    return false
  }

  getCountdown(order: OrderInfo) {

    
    let firstDate = new Date();
    let secondDate = new Date();


    secondDate.setHours(Number.parseInt(order.deliveryTime.split(':')[0]))
    secondDate.setMinutes(Number.parseInt(order.deliveryTime.split(':')[1]))
    const milliDiff: number = firstDate.getTime()
      - secondDate.getTime();
    

    // Converting time into hh:mm:ss format

    // Total number of seconds in the difference
    const totalSeconds = Math.abs(Math.floor(milliDiff / 1000));

    // Total number of minutes in the difference
    const totalMinutes = Math.abs(Math.floor(totalSeconds / 60));

    // Total number of hours in the difference
    const totalHours = Math.abs(Math.floor(totalMinutes / 60));

    // Getting the number of seconds left in one minute
    const remSeconds = totalSeconds % 60;

    // Getting the number of minutes left in one hour
    const remMinutes = totalMinutes % 60;

    let endDate = new Date()
    endDate.setHours(totalHours);
    endDate.setMinutes(remMinutes);
    endDate.setSeconds(remSeconds);
    return endDate;

  }

   public goToBank(){
   this.document.location.href = 'https://joliebank.romanellas.cloud/pay.html';
  }

}
