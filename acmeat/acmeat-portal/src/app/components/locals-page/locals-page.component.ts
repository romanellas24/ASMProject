import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import * as moment from 'moment';
import { map, mergeMap, Observable, switchMap } from 'rxjs';
import { filter, tap } from 'rxjs';
import { Local, Menu, MenuType } from 'src/app/entities/entities';
import { LocalsService } from 'src/app/services/locals.service';
import { MenuService } from 'src/app/services/menu.service';

@Component({
  selector: 'app-locals-page',
  templateUrl: './locals-page.component.html',
  styleUrls: ['./locals-page.component.scss']
})
export class LocalsPageComponent implements OnInit {


  localList$:Observable<Local[]> = new Observable();
  menList$ : Observable<Menu[]> = new Observable();
  constructor(
    private router:Router,
    private activeRoute: ActivatedRoute,
    private localSvc:LocalsService,
    private menuSvc:MenuService) { }

  ngOnInit(): void {

    let city:string = this.activeRoute.snapshot.url[1]?.path;
    
    this.localList$ = this.localSvc.getLocalsByAddress(city);
   this.updateList()
    
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      // map((event:NavigationEnd) => event.url.split('/')[-1]),
      tap(console.log)
    ).subscribe((event:NavigationEnd) =>{
      // debugger
      let newUrl = event.url.split('/').slice(-1)[0]
      this.localList$ = this.localSvc.getLocalsByAddress(newUrl);
      this.updateList()
    })
  }

  updateList():void{
     this.menList$ = this.localList$.pipe(
      mergeMap(locals => locals),
      tap(console.log),
      switchMap(local => 
        
           this.menuSvc.getMenusByLocalId(local.id)


      ),
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
   console.log(amIBetween);
   
   
   return amIBetween;//  returns false.  if date ignored I expect TRUE
  }

}
