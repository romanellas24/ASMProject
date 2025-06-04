import { Component, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Menu, OrderInfo, UserInfo } from 'src/app/entities/entities';
import { MenuService } from 'src/app/services/menu.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-menu-detail',
  templateUrl: './menu-detail.component.html',
  styleUrls: ['./menu-detail.component.scss']
})
export class MenuDetailComponent implements OnInit {

  form: FormGroup = this.fb.group(
    {
      quantity: new FormControl(1,[Validators.required]),
      deliveryTime: new FormControl("",[Validators.required])
    }
  )
  isLoading:boolean = false



  menu$: Observable<Menu> = new Observable()
  constructor(
    private activatedRoute: ActivatedRoute,
    private menuSvc: MenuService,
    private orderSvc: OrderService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    let menuId: number = +this.activatedRoute.snapshot.url[3]?.path;
    this.menu$ = this.menuSvc.getMenuDetailById(menuId);
  }

  async submitOrder(menu: Menu) {
    this.isLoading=true
    let quantity = this.form.get('quantity')?.value
    let deliveryTime = this.form.get('deliveryTime')?.value
    let userString = localStorage.getItem("user")
    let user: UserInfo | null = null
    if (userString != null)
      user = JSON.parse(userString)
    else
      throw "USER IS NOT DEFINED!!!"

    let order: OrderInfo = {
      id: 0,
      deliveryTime: deliveryTime,
      purchaseTime: "",
      transactionId: 0,
      userId: Number(user?.id),
      menuId: menu.id,
      price: menu.price * quantity,
      localId: menu.localId,
      quantity: quantity



    }
    let response = await this.orderSvc.createOrder(order).toPromise();

    if (response?.message == "OK") {
      window.alert("order placed!!")
      this.router.navigate(["/"])
    }
    this.isLoading=false

  }

}
