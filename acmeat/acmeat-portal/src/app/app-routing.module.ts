import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { LoginComponent } from './components/login/login.component';
import { AccountCreationComponent } from './components/account-creation/account-creation.component';
import { LocalsPageComponent } from './components/locals-page/locals-page.component';
import { MenuDetailComponent } from './components/menu-detail/menu-detail.component';

const routes: Routes = [
  {path: '', component: HomepageComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: AccountCreationComponent},
  {path: 'locals', component:LocalsPageComponent},
  {path:'locals/menu/:id',component: MenuDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
