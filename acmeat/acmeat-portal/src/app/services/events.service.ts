import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  isSideMenuEnabled$ :BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  isCartMenuEnabled$ :BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  filters$:BehaviorSubject<FormGroup> = new BehaviorSubject<FormGroup>(new FormGroup({}))

  constructor() { }
}
