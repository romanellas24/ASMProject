import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { LoginComponent } from './components/login/login.component';
import { AccountCreationComponent } from './components/account-creation/account-creation.component';
import { LocalsPageComponent } from './components/locals-page/locals-page.component';
import { MenuDetailComponent } from './components/menu-detail/menu-detail.component';
import { OrderListComponent } from './components/order-list/order-list.component';
import { AuthGuard } from './guards/auth.guard';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';


const routes: Routes = [
  {path: '', component: HomepageComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: AccountCreationComponent},
  {path: 'locals/:city', component:LocalsPageComponent},
  {path:'orders',component:OrderListComponent,canActivate:[AuthGuard]},
  {path:'locals/:city/menu/:id',component: MenuDetailComponent},
  { path: 'pageNotFound', component: PageNotFoundComponent },
  { path: '**', redirectTo: 'pageNotFound' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
