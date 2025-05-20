import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Menu } from 'src/app/entities/entities';
import { MenuService } from 'src/app/services/menu.service';

@Component({
  selector: 'app-menu-detail',
  templateUrl: './menu-detail.component.html',
  styleUrls: ['./menu-detail.component.scss']
})
export class MenuDetailComponent implements OnInit {


  menu$:Observable<Menu> = new Observable()
  constructor(private activatedRoute:ActivatedRoute,
    private menuSvc:MenuService
  ) { }

  ngOnInit(): void {
     let menuId:number = +this.activatedRoute.snapshot.url[3]?.path;
    debugger
    this.menu$ = this.menuSvc.getMenuDetailById(menuId);
  }

}
