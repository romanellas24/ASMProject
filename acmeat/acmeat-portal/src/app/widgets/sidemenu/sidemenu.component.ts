import { Component, OnInit } from '@angular/core';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss']
})
export class SidemenuComponent implements OnInit {

  constructor(private eventsService:EventsService) { }

  ngOnInit(): void {
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

}
