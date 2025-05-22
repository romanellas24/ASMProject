import { Component, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Menu, Order, UserInfo } from 'src/app/entities/entities';
import { MenuService } from 'src/app/services/menu.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-menu-detail',
  templateUrl: './menu-detail.component.html',
  styleUrls: ['./menu-detail.component.scss']
})
export class MenuDetailComponent implements OnInit {

  quantity: FormControl = new FormControl();
  deliveryTime : FormControl = new FormControl();


  menu$:Observable<Menu> = new Observable()
  constructor(
    private activatedRoute:ActivatedRoute,
    private menuSvc:MenuService,
    private orderSvc:OrderService
  ) { }

  ngOnInit(): void {
     let menuId:number = +this.activatedRoute.snapshot.url[3]?.path;
    debugger
    this.menu$ = this.menuSvc.getMenuDetailById(menuId);
  }

  async submitOrder(menu:Menu){
    let quantity = this.quantity.value
    let deliveryTime = this.deliveryTime.value
    let userString = localStorage.getItem("user")
    let user:UserInfo | null= null
    if(userString != null)
     user = JSON.parse(userString)
    else
      throw "USER IS NOT DEFINED!!!"

    let order :Order = {
      id:0,
      deliveryTime : deliveryTime,
      purchaseTime : "",
      transactionId: 0,
      userId: Number(user?.id),
      menuId: menu.id,
      price: menu.price * quantity,
      localId: menu.localId,
      quantity:quantity


      
    }
    let response = await this.orderSvc.createOrder(order).toPromise();

    if(response?.message == "OK"){
      window.alert("order placed!!")
    }


  }

}
