import { RouteRequest } from './../models/routeRequest/route-request';
/// <reference types="@types/google.maps" />
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RouteTravelMode } from '../models/routeRequest/route-travel-mode';
import { RoutingPreference } from '../models/routeRequest/routing-preference';
import { Observable, of } from 'rxjs';
import { RouteRequestDocument } from '../models/routeRequest/route-request-document';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RouteSearchService {

  DEFAULT_TRAVEL_MODE: RouteTravelMode = RouteTravelMode.TRANSIT;
  DEFAULT_ROUTING_PREFERENCE: RoutingPreference = RoutingPreference.ROUTING_PREFERENCE_UNSPECIFIED;
  DEFAULT_ALTERNATIVE_ROUTES: boolean = true;

  currentRouteRequest!: RouteRequest;

  constructor(private http: HttpClient, private authSvc: AuthenticationService) { }

  public findRoute(request: RouteRequest): Observable<any> {

    const params = new HttpParams()
                  .set('origin', request.origin.placeId)
                  .set('destination', request.destination.placeId)
                  .set('units', request.units!);

    if(request.departureTime != undefined) {
      params.set('departureTime', request.departureTime);
    }

    if(request.arrivalTime !=undefined) {
      params.set('arrivalTime', request.arrivalTime);
    }

    if(request.travelMode !=undefined) {
      params.set('travelMode', request.travelMode);
    }

    if(request.computeAlternativeRoutes !=undefined) {
      params.set('computeAlternativeRoutes', request.computeAlternativeRoutes);
    }

    const headers = new HttpHeaders().set('content-type', 'application/json');
    this.authSvc.addAuthorizationHeader(headers);

    const httpOptions = {
      headers: headers,
      params: params
    }
    return this.http.get<any>("/api/routes/routeSearch", httpOptions);
  }

  public saveRoute(request: RouteRequest, index: number): Observable<any>{
    const headers = new HttpHeaders().set('content-type', 'application/json');
    this.authSvc.addAuthorizationHeader(headers);
    const body: RouteRequestDocument = {routeRequest: request, indexes: [index]};
    return this.http.post<any>("/api/routes/saveRoute", body, {headers: headers});
  }

  public getSavedRoutes(): Observable<any> {
    const headers = new HttpHeaders();
    this.authSvc.addAuthorizationHeader(headers);
    return this.http.get<any>("/api/routes/savedRoutes", {headers: headers});
  }

  public updateSavedIndex(id: String, index: number) {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    this.authSvc.addAuthorizationHeader(headers);
    const body = JSON.stringify(index);
    return this.http.put<any>("/api/routes/saveRoute/"+id, body, {headers: headers});
  }

  public createISOString(date:string, time: string) {
    return new Date(date + ' ' + time).toISOString();
  }

  public computeArrivalTimes(departTime: string, routes: any) {
    let time = new Date(departTime);
    let arrivals=[];
    for(let route of routes) {
      let addedTime = Number(route.localizedValues.duration.text.split(' ')[0]);
      let arrivalTime = new Date();
      arrivalTime.setMinutes(time.getMinutes()+addedTime);
      arrivals.push(arrivalTime);
    }
    return arrivals
  }

  public computeTravelTimes(routes: any) {
    let travelTimes: Number[][] = [];
    for(let route of routes) {
      let stepTravelTimes: Number[] = [];
      for(let step of route.legs[0].stepsOverview.multiModalSegments) {
        let sum = 0;
        if(step.stepStartIndex != step.stepEndIndex) {
          for(let i: number = step.stepStartIndex; i <= step.stepEndIndex; i++) {
            sum += Number(route.legs[0].steps[i].localizedValues.staticDuration.text.split(' ')[0]);
          }
        } else if(step.stepStartIndex == step.stepEndIndex) {
          sum += Number(route.legs[0].steps[step.stepStartIndex].localizedValues.staticDuration.text.split(' ')[0]);
        }
        stepTravelTimes.push(sum);
      }
      travelTimes.push(stepTravelTimes);
    }
    return travelTimes;
  }
}
