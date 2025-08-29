import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserInfo } from 'src/app/entities/entities';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoading:boolean = false;

  form:FormGroup = this.fb.group(
        {
          email: new FormControl('',[Validators.email,Validators.required]),
          password: new FormControl('',[Validators.required]),
         
        }
      )

  constructor( private userSvc:UserService,
      private fb:FormBuilder,
      private router: Router) { }

  ngOnInit(): void {
  }

  public goToSignUp():void{
    this.router.navigate(['/signup']);
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

      window.alert("the form is invalid."); 
      console.log(this.form.controls);
      return false;
    }else{
      return true;
    }
  }

  public async login(){
    
    let email:string = this.form.get('email')?.value
    let pwd:string = this.form.get('password')?.value
    
    this.isLoading=true;
    if(this.isFormValid()){
      let user : UserInfo | undefined | void;
      
        user = await this.userSvc.login(email,pwd).toPromise().catch(error => window.alert(error.statusText))

      
      
      if(user!= undefined){
        localStorage.setItem("user",JSON.stringify(user))
        window.alert("Authentication is successfull!")
        this.userSvc.setUserAuthenticated(true);
        this.router.navigate(["/"])
      }else{
        window.alert("this user doesn't exist")
      }

      this.isLoading=false;

    }

  }
}
