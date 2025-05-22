import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EventsService } from 'src/app/services/events.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss']
})
export class SidemenuComponent implements OnInit {


  isAuthenticated$ : Observable<boolean> = new Observable();

  constructor(
    private eventsService:EventsService,
    private userSvc:UserService) { }

  ngOnInit(): void {
    this.isAuthenticated$ = this.userSvc.isAuthenticated$
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

}
