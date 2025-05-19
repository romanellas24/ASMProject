import { Component, OnInit } from '@angular/core';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private eventsService:EventsService) { }

  ngOnInit(): void {
  }

  toggleMenu():void{  
    let oldValue :boolean = this.eventsService.isSideMenuEnabled$.value;
    this.eventsService.isSideMenuEnabled$.next(!oldValue)
  }

}
