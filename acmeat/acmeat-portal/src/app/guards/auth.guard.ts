// auth.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

import { UserService } from '../services/user.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private authService: UserService, private router: Router) {}

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        if (this.authService.getUserInfo()) {
            return true; 
        } else {
            this.router.navigate(['/login']); 
            return false;
        }
    }
}