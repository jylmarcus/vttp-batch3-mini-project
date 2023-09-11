import { Component, OnInit } from '@angular/core';
import { RouteSearchService } from 'src/app/services/route-search.service';

@Component({
  selector: 'app-saved-routes',
  templateUrl: './saved-routes.component.html',
  styleUrls: ['./saved-routes.component.css']
})
export class SavedRoutesComponent implements OnInit{

  constructor(private routeSearchSvc: RouteSearchService) {}

  ngOnInit(): void {
      //get saved routes by passing user id
      console.log(this.routeSearchSvc.getSavedRoutes());
  }
}
