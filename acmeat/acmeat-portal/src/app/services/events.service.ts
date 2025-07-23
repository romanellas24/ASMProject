import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  isSideMenuEnabled$ :BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  isCartMenuEnabled$ :BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  filters$:BehaviorSubject<FormGroup> = new BehaviorSubject<FormGroup>(new FormGroup({}))
  isAddressChanged$:Subject<string> = new Subject<string>()

  constructor() { }
}
