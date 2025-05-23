import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { FooterComponent } from './components/footer/footer.component';
import { LoginComponent } from './components/login/login.component';
import { SearchComponent } from './widgets/search/search.component';
import { SidemenuComponent } from './widgets/sidemenu/sidemenu.component';
import { FilterComponent } from './components/filter/filter.component';
import { MenuDetailComponent } from './components/menu-detail/menu-detail.component';
import { OrdersComponent } from './components/orders/orders.component';
import { AccountCreationComponent } from './components/account-creation/account-creation.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SliderComponent } from './widgets/slider/slider.component';
import { HttpClientModule } from '@angular/common/http';
import { LocalsPageComponent } from './components/locals-page/locals-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { OrderListComponent } from './components/order-list/order-list.component';
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomepageComponent,
    FooterComponent,
    LoginComponent,
    SearchComponent,
    SidemenuComponent,
    FilterComponent,
    MenuDetailComponent,
    OrdersComponent,
    AccountCreationComponent,
    SliderComponent,
    LocalsPageComponent,
    OrderListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
