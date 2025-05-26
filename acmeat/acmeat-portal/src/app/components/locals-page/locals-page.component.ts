import { Component, OnDestroy, OnInit } from '@angular/core';
import { Form, FormGroup } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import * as moment from 'moment';
import { combineLatest, distinctUntilChanged, map, mergeMap, Observable, repeatWhen, Subscription, switchMap } from 'rxjs';
import { filter, tap } from 'rxjs';
import { Local, Menu, MenuType } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { LocalsService } from 'src/app/services/locals.service';
import { MenuService } from 'src/app/services/menu.service';

@Component({
  selector: 'app-locals-page',
  templateUrl: './locals-page.component.html',
  styleUrls: ['./locals-page.component.scss']
})
export class LocalsPageComponent implements OnInit,OnDestroy {


  localList$:Observable<Local[]> = new Observable();
  menList$ : Observable<Menu[]> = new Observable();
  menuType:string = ""
  hourType:string = ""
  subscriptions: Subscription [] =[]


  constructor(
    private router:Router,
    private activeRoute: ActivatedRoute,
    private localSvc:LocalsService,
    private eventSvc :EventsService,
    private menuSvc:MenuService) { }
  ngOnDestroy(): void {
   this.subscriptions.forEach(
    sub => sub.unsubscribe()
   )
  }

  ngOnInit(): void {

    let city:string = this.activeRoute.snapshot.url[1]?.path;
    
    this.localList$ = this.localSvc.getLocalsByAddress(city);
    // debugger
    // this.localList$.pipe(
    //   repeatWhen(() => this.eventSvc.filters$),
      
    //   switchMap(local => local),
    //   filter((local:Local) => this.hourType =="Cena" ? this.isDinner(local.openingTime,local.closingTime): !this.isDinner(local.openingTime,local.closingTime) )
    // )

    this.subscriptions.push(
    this.eventSvc.filters$.pipe(
      map((formGroup :any) => {
        this.menuType= formGroup.menuType
        this.hourType = formGroup.hoursType

        return formGroup
      })
    ).subscribe()
    );
  

   
    
        this.updateList()
   this.subscriptions.push(
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      distinctUntilChanged()
      // map((event:NavigationEnd) => event.url.split('/')[-1]),
      // tap(console.log)
    ).subscribe((event:any) =>{
      // debugger
      let newUrl = event.url.split('/').slice(-1)[0]
      this.localList$ = this.localSvc.getLocalsByAddress(newUrl);
      
      this.updateList()
    }));
  }

  updateList():void{
     this.menList$ = this.localList$.pipe(
     
      repeatWhen(() => this.eventSvc.filters$),
       map((locals:Local[]) => {

        if(this.hourType != undefined){
          locals = locals.filter( (local:Local) => this.hourType =="Cena" ? this.isDinner(local.openingTime,local.closingTime): !this.isDinner(local.openingTime,local.closingTime))
          return locals;
        }else{
          return locals;
        }
      }),
      mergeMap(locals => locals),
      // tap(console.log),
      switchMap(local => 
        
           this.menuSvc.getMenusByLocalId(local.id)


      ),
      
      map((menus:Menu[] ) => {
        // debugger

        if(this.menuType!= undefined){
          menus = menus.filter( menu => menu.type == this.menuType)
          return menus
        }else{
          return menus
        }
      })
      // tap(console.log)
    )


  }

  public isFish(menuType:string){
      return menuType == MenuType.FISH
  }

  public isDinner(openingTime:string, closingTime:string){
  
var startTime = moment(openingTime, "HH:mm");
var endTime = moment(closingTime, "HH:mm");

    var dinnerStart= moment("19:00", "HH:mm");
    var dinnerEnds = moment("22:00","HH:mm");
   let amIBetween = dinnerStart <= startTime && dinnerEnds >= endTime 
  //  console.log(amIBetween);
   
   
   return amIBetween;//  returns false.  if date ignored I expect TRUE
  }

}
