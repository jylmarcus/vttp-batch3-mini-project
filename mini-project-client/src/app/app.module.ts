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

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    RouteDisplayComponent,
    SavedRoutesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
