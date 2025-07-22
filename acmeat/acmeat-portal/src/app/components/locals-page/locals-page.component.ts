import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Form, FormGroup } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import * as moment from 'moment';
import { BehaviorSubject, combineLatest, distinctUntilChanged, flatMap, map, mergeMap, Observable, repeatWhen, shareReplay, Subject, Subscription, switchMap, take } from 'rxjs';
import { filter, tap } from 'rxjs';
import { Local, Menu, MenuType } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { LocalsService } from 'src/app/services/locals.service';
import { MenuService } from 'src/app/services/menu.service';
import { FilterComponent } from '../filter/filter.component';

@Component({
  selector: 'app-locals-page',
  templateUrl: './locals-page.component.html',
  styleUrls: ['./locals-page.component.scss']
})
export class LocalsPageComponent implements OnInit,OnDestroy {


  localList$:Observable<Local[]> = new Observable();
  queryUpdated$ :Subject<boolean> = new Subject();
  menList$ : Observable<Menu[]> = new Observable();
  isLoading : boolean = false ;
  menusList:Menu[] =[]
  localList:Local[] =[]
  menuType:string = ""
  hourType:string = ""
  subscriptions: Subscription [] =[];
  @ViewChild(FilterComponent) filterComponent!: FilterComponent;

  city:string="";


  constructor(
    private router:Router,
    private activeRoute: ActivatedRoute,
    private localSvc:LocalsService,
    private eventSvc :EventsService,
    private route : ActivatedRoute,
    private menuSvc:MenuService) { }
  ngOnDestroy(): void {
   this.subscriptions.forEach(
    sub => sub.unsubscribe()
   )
  }

  ngOnInit(): void {
     

     this.city= this.activeRoute.snapshot.url[1]?.path;


    //  debugger
     this.subscriptions.push(
    this.queryUpdated$
    .subscribe(() =>{
     
      // debugger
      this.localList$ = this.localSvc.getLocalsByAddress(this.city).pipe(
         tap(() => this.isLoading = true),
         shareReplay(1),
         take(1),
         distinctUntilChanged(),
       map((locals:Local[]) => {
        // debugger
        // if(this.hourType != undefined){
        //   locals = locals.filter( (local:Local) => this.hourType =="Cena" ? this.isDinner(local.openingTime,local.closingTime): !this.isDinner(local.openingTime,local.closingTime))
        //   this.localList = locals
        //   this.localList = [...new Set(this.localList)]

        //   return locals;
        // }else{
          this.localList = this.localList.concat([...locals])
          this.localList = [...new Set(this.localList)]
          return locals;
        // }

      }),
    );

     this.menList$ = this.localList$.pipe(
      
     
      flatMap(locals => locals),
      // tap(console.log),
      flatMap(local => 
        
           this.menuSvc.getMenusByLocalId(local.id)


      ),
      tap(() => this.isLoading =false),
      map((menus:Menu[] ) => {
        // debugger

        // if(this.menuType!= undefined){
        //   menus = menus.filter( menu => menu.type == this.menuType)
        //   this.menusList = this.menusList.concat([...menus])
        //   this.menusList = [...new Set(this.menusList)]
        //   return menus
        // }else{
          this.menusList = this.menusList.concat([...menus])
          this.menusList = [...new Set(this.menusList)]
          // this.isLoading = false;
          return menus
        // }
      })
    )
    // this.isLoading = false;
    }

    ))
    


    // this.subscriptions.push(
    // this.eventSvc.filters$.pipe(
    //   map((formGroup :any) => {
    //     // debugger
    //     // if(formGroup.menuType != "" && (this.menuType != formGroup.menuType)){
    //     //   this.menusList = [];
    //     // }

    //     this.menuType= formGroup.menuType
    //     this.hourType = formGroup.hoursType

    //   this.localList = []
    //   this.menusList = [];
    //     // this.menusList = []
    //     this.queryUpdated$.next(true);

    //     return formGroup
    //   })
    // ).subscribe()
    // );

    this.queryUpdated$.next(true);
    
   this.subscriptions.push(
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      distinctUntilChanged(),
      // tap(console.log)
    ).subscribe((event:any) =>{
      // debugger

      this.localList = []
      this.menusList = [];
      this.city = event.url.split('/').slice(-1)[0]
      this.queryUpdated$.next(true);
      
    }));
  }

  getLocal(localList:Local[],localId:number):Local | undefined{
    return localList.find(local => local.id == localId)
  }



  public isFish(menuType:string){
      return menuType == MenuType.FISH
  }

  navigateTo(menuId:number):void{
    // debugger
    // this.filterComponent.reset()
    this.queryUpdated$.next(true);
    this.router.navigate(['menu/'+menuId],{ relativeTo: this.route })
    
  }

  public isDinner(openingTime:string | undefined, closingTime:string | undefined){
  if(openingTime != undefined && closingTime != undefined){
    var startTime = moment(openingTime, "HH:mm");
var endTime = moment(closingTime, "HH:mm");

    var dinnerStart= moment("19:00", "HH:mm");
    var dinnerEnds = moment("22:00","HH:mm");
   let amIBetween = dinnerStart <= startTime && dinnerEnds >= endTime 
  //  console.log(amIBetween);
   
   
   return amIBetween;//  returns false.  if date ignored I expect TRUE
  
  }else{
    return false
  }
}

}
