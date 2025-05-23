import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderInfo, UserInfo } from 'src/app/entities/entities';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {

  orderList$ : Observable<OrderInfo[]> = new Observable()
  constructor(
    private userSvc: UserService,
    private orderSvc:OrderService
  ) { }

  ngOnInit(): void {

    if(this.userSvc.isAuthenticated$.value == true){
      let user : UserInfo | undefined = this.userSvc.getUserInfo();

      if(user != undefined)
        this.orderList$ = this.orderSvc.getOrdersByUserId(user.id)
    }
      
  }

}
