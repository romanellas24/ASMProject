import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject, debounceTime, filter, mergeMap, Observable, tap, withLatestFrom } from 'rxjs';
import { delay, distinctUntilChanged, map } from 'rxjs';
import { Local } from 'src/app/entities/entities';
import { LocalsService } from 'src/app/services/locals.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  address:FormControl = new FormControl('')
  addressValue$:Observable<string> = new Observable<string>()
  localsList$ :Observable<Local[]> = new Observable<Local[]>()
  cityList$:BehaviorSubject<string[]> = new BehaviorSubject<string[]>(
   []
  )

  allCities:string[] = [
    "Bologna",
    "Bolona",
    "Roma",
    "San Salvo"
  ]

  constructor(
    private localSvc: LocalsService,
    private router: Router
  ) { }


  ngOnInit(): void {

    this.addressValue$ = this.address.valueChanges.pipe(
      distinctUntilChanged(),
      debounceTime(500),
      map((value :string) => value)      
    )

    this.addressValue$.pipe(
      map((address:string)=>{
        let cityList:string[]=[]
        if(address != ''){
          cityList = this.allCities
                      .filter(city =>{
                          let addressTolowecase =  address
                         .toLowerCase()

                         let cityToLoweCase = city.toLowerCase() 
                         
                         return cityToLoweCase.includes(addressTolowecase);

                      }
                        );
        }
         
        this.cityList$.next(cityList)

      })
    ).subscribe()

    


   
     


  }



    public goToLocalList(city:string){
       this.router.navigate(['locals/'+city])
       
       
    }
}
