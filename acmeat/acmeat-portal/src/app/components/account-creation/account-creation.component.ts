import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { GeneralResponse, UserInfo } from 'src/app/entities/entities';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-account-creation',
  templateUrl: './account-creation.component.html',
  styleUrls: ['./account-creation.component.scss']
})
export class AccountCreationComponent implements OnInit {

  constructor(
    private userSvc:UserService,
    private fb:FormBuilder,
    private router: Router) { }

    form:FormGroup = this.fb.group(
      {
        email: new FormControl('',[Validators.email,Validators.required]),
        password: new FormControl('',[Validators.required]),
        address: new FormControl('',[Validators.required])
      }
    )

    validationError$ = new Observable()
    isAuthenticated: boolean = false;

  ngOnInit(): void {

   
  }

  public isFormValid():boolean{
    if(this.form.invalid){
      if(!!this.form.controls['email'].errors){
        window.alert("the e-mail is invalid");
        return false;
      }

      if(!!this.form.controls['password'].errors){
        window.alert("password is required!")
        return false;
      }

      if(!!this.form.controls['address'].errors){
        window.alert("address is required!");
        return false;
      }

      window.alert("the form is invalid."); 
      console.log(this.form.controls);
      return false;
    }else{
      return true;
    }
  }

  public async submit(){
    if(this.isFormValid()){
      
      let user:UserInfo = {
        id: 0,
        address: this.form.get('address')?.value,
        mail : this.form.get('email')?.value,
        pwd : this.form.get('password')?.value

      }

    let response:GeneralResponse | undefined= await this.userSvc.createUser(user).toPromise()

      if(response?.message == "OK"){
         window.alert("user created succesfully")
      }else{
        window.alert("there was a problem: " + response?.message)
      }
     this.isAuthenticated = true;
    }
  }


  public goToHomepage(){
    this.router.navigate(['/'])

  }
}
