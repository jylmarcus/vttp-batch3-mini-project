import { Observable, catchError, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GooglePlacesService {
  private placesService!: google.maps.places.PlacesService;

  constructor(private http: HttpClient) {
    this.placesService = new google.maps.places.PlacesService(document.createElement('div'))
  }

  public getPlaceDetails(placeId: string) {
    const request: google.maps.places.PlaceDetailsRequest = {
      placeId: placeId,
      fields: ['name']
    };

    return new Observable((observer) => {
      this.placesService.getDetails(request, (place, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK) {
          const result = {
            name: place?.name
          }
          observer.next(result);
          observer.complete();
        } else {
          observer.error(status);
        }
      });
    }).pipe(
      catchError((error)=> {
        return throwError(() => error);
      })
    );
  }
}
