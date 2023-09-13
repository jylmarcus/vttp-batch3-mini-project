import { GooglePlacesService } from './../../../services/google-places.service';
/// <reference types="@types/google.maps" />
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RouteTravelMode } from 'src/app/models/routeRequest/route-travel-mode';
import { RouteSearchService } from 'src/app/services/route-search.service';

@Component({
  selector: 'app-saved-routes',
  templateUrl: './saved-routes.component.html',
  styleUrls: ['./saved-routes.component.css'],
})
export class SavedRoutesComponent implements OnInit {
  constructor(private routeSearchSvc: RouteSearchService, private googlePlacesSvc: GooglePlacesService, private router: Router) {}

  savedRequests!: any;

  ngOnInit(): void {
    //get saved routes by passing user id
    this.routeSearchSvc.getSavedRoutes().subscribe((data) => {
      this.savedRequests = [];
      //to access indexes array: data[i].indexes
      //to access routes: data[i].response.routes[data[i].indexes[j]]
      for(let i = 0; i < data.length; i++) {
        this.savedRequests.push({requestId: data[i]._id, request: data[i].routeRequest, indexes: data[i].indexes});
      }
      this.convertPlaceIdsToNames();
    });
  }

  convertPlaceIdsToNames() {
    for(let savedRequest of this.savedRequests) {
      const originPlaceId = savedRequest.request.origin.placeId;
      const destinationPlaceId = savedRequest.request.destination.placeId;

      this.googlePlacesSvc.getPlaceDetails(originPlaceId).subscribe((originPlace: any) => {
        savedRequest.request.origin.placeName = originPlace.name;
      });

      this.googlePlacesSvc.getPlaceDetails(destinationPlaceId).subscribe((destinationPlace: any) => {
        savedRequest.request.destination.placeName = destinationPlace.name;
      })
    }
  }

  showSavedRoutes(index: number) {
    //savedRequests[index] is the main target
    this.router.navigate(['routes/results'], {
      queryParams: {
        origin: this.savedRequests[index].request.origin.placeId,
        originName: this.savedRequests[index].request.origin.placeName,
        destination: this.savedRequests[index].request.destination.placeId,
        destinationName: this.savedRequests[index].request.destination.placeName,
        time: this.savedRequests[index].request.departureTime != null ? this.savedRequests[index].request.departureTime : this.savedRequests[index].request.arrivalTime,
        flag: this.savedRequests[index].request.departureTime != null ? true : false,
        travelMode: <RouteTravelMode> this.savedRequests[index].request.travelMode,
        computeAlternativeRoutes: true,
        currentObjId: this.savedRequests[index].requestId,
        indexes: this.savedRequests[index].indexes
      },
    });
  }
}
