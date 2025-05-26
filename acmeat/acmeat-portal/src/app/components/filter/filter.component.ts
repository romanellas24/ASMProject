import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit,OnDestroy {

  menuTypes:string[] = ["Carne","Pesce"]
  hoursTypes:string[] = ["Pranzo","Cena"]
  subscriptions:Subscription[] = []

  filters: FormGroup =new FormGroup({
    
    menuType : new FormControl(''),
    hoursType : new FormControl('')
  }
  )

  constructor(private eventsSvc:EventsService) { }
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }

  ngOnInit(): void {
    this.subscriptions.push(
    this.filters.valueChanges.subscribe(
      (formGroup:FormGroup) => this.eventsSvc.filters$.next(formGroup)
    )
  );
  }

}
