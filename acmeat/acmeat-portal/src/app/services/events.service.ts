import { Injectable } from '@angular/core';
import { Observable } from 'ol';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  isSideMenuEnabled$ :BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() { }
}
