import { RoutingPreference } from './../../../models/routeRequest/routing-preference';
import { RouteRequest } from './../../../models/routeRequest/route-request';
import { RouteSearchService } from 'src/app/services/route-search.service';
/// <reference types="@types/google.maps" />
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { RouteTravelMode } from 'src/app/models/routeRequest/route-travel-mode';

@Component({
  selector: 'app-route-display',
  templateUrl: './route-display.component.html',
  styleUrls: ['./route-display.component.css'],
})
export class RouteDisplayComponent implements OnInit {
  routes!: any;
  departureTime!: string;
  arrivalTime!: string;
  originName!: string;
  destinationName!: string;
  travelMode!: RouteTravelMode;
  RouteTravelMode = RouteTravelMode;
  travelTimes: any;
  totalTravelTimes: any;
  arrivalTimes: any;
  currentRequest!: RouteRequest;
  currentObjId!: string;
  savedRouteIndexes!: number[];
  currentYear = new Date().getFullYear;

  constructor(
    private routeSearchSvc: RouteSearchService,
    private location: Location,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.originName = this.route.snapshot.queryParams['originName']!;
    this.destinationName = this.route.snapshot.queryParams['destinationName']!;
    if (this.route.snapshot.queryParams['flag'] == "true") {
      this.departureTime = this.route.snapshot.queryParams['time']!;
    } else {
      this.arrivalTime = this.route.snapshot.queryParams['time']!;
    }
    this.travelMode = <RouteTravelMode>(
      this.route.snapshot.queryParams['travelMode']!
    );
    this.currentRequest = {
      origin: { placeId: this.route.snapshot.queryParams['origin']! },
      destination: { placeId: this.route.snapshot.queryParams['destination']! },
      travelMode: this.travelMode,
      routingPreference:
        this.travelMode == RouteTravelMode.DRIVE
          ? RoutingPreference.TRAFFIC_AWARE
          : RoutingPreference.ROUTING_PREFERENCE_UNSPECIFIED,
      departureTime:
        this.departureTime != undefined ? this.departureTime : null,
      arrivalTime: this.arrivalTime != undefined ? this.arrivalTime : null,
      computeAlternativeRoutes: Boolean(
        this.route.snapshot.queryParams['computeAlternativeRoutes']!
      ),
      units: 'METRIC',
    };
    this.routeSearchSvc.findRoute(this.currentRequest).subscribe({
      next: (result) => {
        this.routes = result.routes;
        this.arrivalTimes = this.routeSearchSvc.computeArrivalTimes(
          this.departureTime,
          this.routes
        );
        this.travelTimes = this.routeSearchSvc.computeTravelTimes(this.routes);
        this.totalTravelTimes = this.routeSearchSvc.computeTotalTravelTimes(this.travelTimes);
      },
    });
    if(this.route.snapshot.queryParams['currentObjId'] != undefined || this.route.snapshot.queryParams['currentObjId'] != null) {
      this.currentObjId = this.route.snapshot.queryParams['currentObjId'];
    }

    if(this.route.snapshot.queryParams['indexes'] != undefined || this.route.snapshot.queryParams['indexes'] != null) {
      this.savedRouteIndexes = this.route.snapshot.queryParams['indexes'];
    }
  }

  saveRoute(index: number) {
    if (this.currentObjId == undefined) {
      this.routeSearchSvc.saveRoute(this.currentRequest, index).subscribe({
        next: (result) => {
          this.currentObjId = result.objId;
        },
      });
    } else {
      this.routeSearchSvc
        .updateSavedIndex(this.currentObjId, index)
        .subscribe();
    }
  }

  deleteRoute(i: number) {
    this.routeSearchSvc.deleteSavedRoute(this.currentObjId, this.savedRouteIndexes[i]).subscribe((data) => {
      this.savedRouteIndexes = [...this.savedRouteIndexes.splice(i, 1)];
      if(this.savedRouteIndexes.length == 1) {
        this.savedRouteIndexes = [];
        this.viewSavedRoutes();
      }
    });
  }

  goBack() {
    this.router.navigate(['routes/search']);
  }

  viewSavedRoutes() {
    this.router.navigate(['routes/savedRoutes']);
  }
}
