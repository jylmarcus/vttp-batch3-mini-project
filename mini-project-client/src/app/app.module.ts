import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponent } from './components/routeSearch/search/search.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouteDisplayComponent } from './components/routeDisplay/route-display/route-display.component';
import { SavedRoutesComponent } from './components/savedRoutes/saved-routes/saved-routes.component';
import { LoginComponent } from './components/login/login/login.component';
import { RegisterComponent } from './components/register/register/register.component';
import { LogoutComponent } from './components/logout/logout/logout.component';
import { NavBarComponent } from './components/navBar/nav-bar/nav-bar.component';
import { MaterialModule } from './modules/material/material.module';
import { HomeComponent } from './components/home/home/home.component';
import {
  MatMomentDatetimeModule,
  MomentDatetimeAdapter,
} from '@mat-datetimepicker/moment';
import {
  DatetimeAdapter,
  MAT_DATETIME_FORMATS,
} from '@mat-datetimepicker/core';
import {
  MAT_MOMENT_DATE_ADAPTER_OPTIONS,
  MomentDateAdapter,
} from '@angular/material-moment-adapter';
import { DateAdapter } from '@angular/material/core';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    RouteDisplayComponent,
    SavedRoutesComponent,
    LoginComponent,
    RegisterComponent,
    LogoutComponent,
    NavBarComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    MatMomentDatetimeModule,
  ],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
    },
    {
      provide: DatetimeAdapter,
      useClass: MomentDatetimeAdapter,
    },
    {
      provide: MAT_DATETIME_FORMATS,
      useValue: {
        parse: {
          dateInput: 'YYYY-MM-DD',
          monthInput: 'MMMM',
          timeInput: 'HH:mm:ss',
          datetimeInput: 'YYYY-MM-DD HH:mm:ss',
        },
        display: {
          dateInput: 'YYYY-MM-DD',
          monthInput: 'MMMM',
          datetimeInput: 'YYYY-MM-DD HH:mm',
          timeInput: 'HH:mm:ss',
          monthYearLabel: 'MMM YYYY',
          dateA11yLabel: 'LL',
          monthYearA11yLabel: 'MMMM YYYY',
          popupHeaderDateLabel: 'ddd, DD MMM',
        },
      },
    },
    {
      provide: MAT_MOMENT_DATE_ADAPTER_OPTIONS,
      useValue: {
        useUtc: false,
      },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
