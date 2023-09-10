import { RouteRequest } from './../../../models/routeRequest/route-request';
import { RouteSearchService } from 'src/app/services/route-search.service';
/// <reference types="@types/google.maps" />
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  arrivalTimes: any;
  currentRequest!: RouteRequest;
  currentObjId!: string; //temporary solution

  constructor(
    private routeSearchSvc: RouteSearchService,
    private location: Location,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.originName = this.route.snapshot.queryParams['originName']!;
    this.destinationName = this.route.snapshot.queryParams['destinationName']!;
    if (this.route.snapshot.queryParams['flag']) {
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
      departureTime:
        this.departureTime != undefined ? this.departureTime : undefined,
      arrivalTime: this.arrivalTime != undefined ? this.arrivalTime : undefined,
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
      },
    });
  }

  saveRoute(index: number) {
    if (this.currentObjId == undefined) {
      this.routeSearchSvc.saveRoute(this.currentRequest, index).subscribe({
        next: (result) => {
          this.currentObjId = result.objId;
        }
      });
    } else {
      this.routeSearchSvc.updateSavedIndex(this.currentObjId, index).subscribe();
    }
  }

  goBack() {
    this.location.back();
  }
}
