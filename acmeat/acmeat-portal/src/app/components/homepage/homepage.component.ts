import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Route } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { GeneralResponse, UserInfo } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { OrderService } from 'src/app/services/order.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {
  isAuthenticated$ : Observable<boolean> = new Observable();
  orderId:string="";
  user:UserInfo | undefined;
  subscriptionList$ :Subscription [] =[]
  

  constructor(
    private userSvc:UserService,
    private orderSvc:OrderService,
    private EventSvc: EventsService,
    private route: ActivatedRoute
  ) { }

  
  
  ngOnInit(): void {

    this.isAuthenticated$ = this.userSvc.isAuthenticated$
    this.subscriptionList$.push(
    this.route.queryParams.subscribe(
      params =>{
        let orderIdParam :string | undefined = params["orderId"];
        if(orderIdParam != undefined)
          this.orderId = orderIdParam;
          
        let token:string | null = sessionStorage.getItem(this.orderId);
        
        if(token != null){
          this.subscriptionList$.push(
          this.orderSvc.verifyPayment(this.orderId,token).subscribe( (res:GeneralResponse) =>{

            if(res != null && res.message =="OK"){
              window.alert("The payment was confirmed")
              sessionStorage.removeItem(this.orderId)
            }else{
              window.alert("An error occurred: " + res.message)
            }
          },
        (error) => window.alert(error))
    )
        }
      }
    ))
   
  }

  public searchWithMyAddress(){
    let searchBar :any = document.getElementById('search-bar')
    let userString :string | null = localStorage.getItem("user")
    
    if(userString){
      this.user = JSON.parse(userString)
    }
    
    
    if(searchBar != null){
      // debugger
      searchBar.value= this.user?.address
      if(this.user?.address != undefined)
        this.EventSvc.isAddressChanged$.next(this.user?.address);
      
    }
    //TO DO TEST AFTER LOGIN API
  }

 public  progressScript() {
 
}

}
