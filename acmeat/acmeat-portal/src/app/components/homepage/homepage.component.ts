import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { UserInfo } from 'src/app/entities/entities';
import { EventsService } from 'src/app/services/events.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {
  isAuthenticated$ : Observable<boolean> = new Observable();
  user:UserInfo | undefined;
  

  constructor(
    private userSvc:UserService,
    private EventSvc: EventsService
  ) { }

  
  
  ngOnInit(): void {

    this.isAuthenticated$ = this.userSvc.isAuthenticated$
   
  }

  public searchWithMyAddress(){
    let searchBar :any = document.getElementById('search-bar')
    let userString :string | null = localStorage.getItem("user")
    
    if(userString){
      this.user = JSON.parse(userString)
    }
    
    
    if(searchBar != null){
      // debugger
      searchBar.value= this.user?.address
      if(this.user?.address != undefined)
        this.EventSvc.isAddressChanged$.next(this.user?.address);
      
    }
    //TO DO TEST AFTER LOGIN API
  }

 public  progressScript() {
 
}

}
