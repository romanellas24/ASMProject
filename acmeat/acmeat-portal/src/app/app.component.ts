import { Component } from '@angular/core';
import { EventsService } from './services/events.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'amceat-portal';

  isSideMenuOpen$ : Observable<boolean> = this.eventsSevice.isSideMenuEnabled$

  constructor(private eventsSevice:EventsService){

  }


}
