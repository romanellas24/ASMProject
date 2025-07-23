import { Component, LOCALE_ID, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { Dish, Menu, OrderInfo, UserInfo } from 'src/app/entities/entities';
import { DishService } from 'src/app/services/dish.service';
import { MenuService } from 'src/app/services/menu.service';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-menu-detail',
  templateUrl: './menu-detail.component.html',
  styleUrls: ['./menu-detail.component.scss']
})
export class MenuDetailComponent implements OnInit {

  form: FormGroup = this.fb.group(
    {
      quantity: new FormControl(1,[Validators.required]),
      deliveryTime: new FormControl("",[Validators.required,Validators.minLength(4)])
    }
  )
  isLoading:boolean = false


  isAuthenticated$ :Observable<boolean> = new Observable();
  menu$: Observable<Menu> = new Observable()
  dishs$: Observable<Dish[]> = new Observable();
  constructor(
    private activatedRoute: ActivatedRoute,
    private menuSvc: MenuService,
    private orderSvc: OrderService,
    private userSvc : UserService,
    private dishSvc :DishService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    // debugger
    this.isAuthenticated$ = this.userSvc.isAuthenticated$
    let menuId: number = +this.activatedRoute.snapshot.url[3]?.path;
    this.menu$ = this.menuSvc.getMenuDetailById(menuId);
    this.dishs$ = this.dishSvc.getDishsByMenuId(menuId);
  }

  async submitOrder(menu: Menu) {

    if(this.isFormValid()){
       this.isLoading=true
    let quantity = this.form.get('quantity')?.value
    let deliveryTime = this.form.get('deliveryTime')?.value
    let user: UserInfo | undefined = this.userSvc.getUserInfo();
    if (user == undefined)
      throw "USER IS NOT DEFINED!!!"

    let order: OrderInfo = {

      deliveryTime: deliveryTime,
      purchaseTime: "",
      transactionId: "",
      userId: Number(user?.id),
      menuId: menu.id,
      deliveryCompanyId:0,
      price: menu.price * quantity,
      localId: menu.localId,
      quantity: quantity



    }
    let response = await this.orderSvc.createOrder(order).toPromise();

    if (response?.message == "OK") {
      window.alert("order placed!!")
      this.router.navigate(["/"])
    }else{
      window.alert("there was an error: "+ response?.message)
    }
    this.isLoading=false
    }

   

  }

     public isFormValid():boolean{
      // debugger
    if(this.form.invalid){
      if(!!this.form.controls['quantity'].errors){
        window.alert("the quantity is invalid");
        return false;
      }

      if(!!this.form.controls['deliveryTime'].errors){
        window.alert("delivery time must be choosen in the right format!")
        return false;
      }
     

      window.alert("the form is invalid."); 
      console.log(this.form.controls);
      return false;
    }

     let deliveryTime =this.form.controls["deliveryTime"].value
      if(!this.isLunch(deliveryTime) && ! this.isDinner(deliveryTime)){
        window.alert("You should choose delivery time between 12-14 or 19-21!  ")
        return false;
      }

      else{
      return true;
    }
  }


    public isDinner(openingTime:string | undefined){
    if(openingTime != undefined ){
    // debugger
        var startTime = moment(openingTime, "HH:mm");
      var dinnerStart= moment("19:00", "HH:mm");
      var dinnerEnds = moment("21:00","HH:mm");
      var past = dinnerStart <= startTime 
      var after = startTime <= dinnerEnds 
     let amIBetween =past && after
     
     
     return amIBetween;//  returns false.  if date ignored I expect TRUE
    
    }else{
      return false
    }
  }

  public isLunch(openingTime:string | undefined){
    if(openingTime != undefined ){
    
        var startTime = moment(openingTime, "HH:mm");
      var dinnerStart= moment("12:00", "HH:mm");
      var dinnerEnds = moment("14:00","HH:mm");
      var past = dinnerStart <= startTime 
      var after = startTime <= dinnerEnds 
     let amIBetween =past && after
     console.log(amIBetween);
     
     
     return amIBetween;//  returns false.  if date ignored I expect TRUE
    
    }else{
      return false
    }
  }

}
