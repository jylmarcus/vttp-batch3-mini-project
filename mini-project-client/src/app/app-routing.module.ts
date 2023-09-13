import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/routeSearch/search/search.component';
import { RouteDisplayComponent } from './components/routeDisplay/route-display/route-display.component';
import { LoginComponent } from './components/login/login/login.component';
import { RegisterComponent } from './components/register/register/register.component';
import { LogoutComponent } from './components/logout/logout/logout.component';
import { SavedRoutesComponent } from './components/savedRoutes/saved-routes/saved-routes.component';
import { HomeComponent } from './components/home/home/home.component';

const routes: Routes = [
  {path: '', component:HomeComponent},
  {path: 'routes/search', component: SearchComponent},
  {path: 'routes/results', component: RouteDisplayComponent},
  {path: 'routes/savedRoutes', component: SavedRoutesComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'logout', component: LogoutComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
