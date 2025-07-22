import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject, debounceTime, filter, mergeMap, Observable, repeatWhen, Subscription, tap, withLatestFrom } from 'rxjs';
import { delay, distinctUntilChanged, map } from 'rxjs';
import { Local } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { LocalsService } from 'src/app/services/locals.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit,OnDestroy {
  subscriptionList :Subscription[] = []
  address:FormControl = new FormControl('')
  addressValue$:Observable<string> = new Observable<string>()
  isAddressChanged$ :Observable<void> = new Observable<void>()
  localsList$ :Observable<Local[]> = new Observable<Local[]>()
  cityList$:BehaviorSubject<string[]> = new BehaviorSubject<string[]>(
   []
  )

  allCities:string[] = [
    "Bologna",
    "Roma",
    "Campobasso",
    "San Salvo"
  ]

  constructor(
    // private localSvc: LocalsService,
    private router: Router,
    private eventSvc:EventsService
  ) { }
  ngOnDestroy(): void {
    this.subscriptionList.forEach(sub => sub.unsubscribe())
  }


  ngOnInit(): void {

    this.addressValue$ = this.address.valueChanges.pipe(
      distinctUntilChanged(),
      debounceTime(1000),
      map((value :string) => value),
      map((address:string)=>{
        let cityList:string[]=[]
        // debugger
        if(address != ''){
          cityList = this.allCities
                      .filter(city =>{
                          let addressTolowecase =  address
                         .toLowerCase()

                         let cityToLoweCase = city.toLowerCase() 
                        //  debugger
                        return addressTolowecase.includes(cityToLoweCase);

                      }
                        );
        }
         
        this.cityList$.next(cityList)
        return address;

      })     
    )

    this.isAddressChanged$ = this.eventSvc.isAddressChanged$.pipe(
      debounceTime(1000),
      map((value) =>{
        // debugger

        let address:string  = value
         let cityList:string[]=[]
        if(address != null && address!= ""){
           cityList = this.allCities
                      .filter(city =>{
                          let addressTolowecase =  address
                         .toLowerCase()

                         let cityToLoweCase = city.toLowerCase() 
                        //  debugger
                         return addressTolowecase.includes(cityToLoweCase);

                      }
                        );
           this.cityList$.next(cityList)
        }
      })
    )

    this.subscriptionList.push(
      this.isAddressChanged$.subscribe()
    )

    this.subscriptionList.push(
      this.addressValue$.subscribe()
    )


    


   
     


  }



    public goToLocalList(city:string){
       this.router.navigate(['locals/'+city])
       
       
    }
}
