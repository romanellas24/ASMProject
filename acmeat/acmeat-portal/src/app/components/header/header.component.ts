import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserInfo } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {


  isAuthenticated$ : Observable<boolean> = new Observable();
  constructor(
    private eventsService:EventsService,
    private userSvc: UserService
  
  ) { }

  ngOnInit(): void {
    if(localStorage.getItem("user") != null){
      this.userSvc.setUserAuthenticated(true);
    }else{
      this.userSvc.setUserAuthenticated(false);
    }
    this.isAuthenticated$ = this.userSvc.isAuthenticated$
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

  getMail(){
    let userString = localStorage.getItem("user")
    
    let user: UserInfo | undefined = undefined;
    if(userString)
      user =JSON.parse(userString)
    
    return user?.mail;

  }

}
