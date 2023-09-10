import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/routeSearch/search/search.component';
import { RouteDisplayComponent } from './components/routeDisplay/route-display/route-display.component';

const routes: Routes = [
  {path: '', redirectTo: '/routes/search', pathMatch: 'full'},
  {path: 'routes/search', component: SearchComponent},
  {path: 'routes/results', component: RouteDisplayComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
