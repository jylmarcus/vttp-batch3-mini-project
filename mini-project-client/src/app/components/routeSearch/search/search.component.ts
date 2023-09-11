/// <reference types="@types/google.maps" />
import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit,
} from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouteSearchService } from 'src/app/services/route-search.service';
import { RouteTravelMode } from 'src/app/models/routeRequest/route-travel-mode';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent implements OnInit, AfterViewInit {
  @ViewChild('originAutocomplete')
  originAutocompleteInput!: ElementRef;
  originAutocompleter!: google.maps.places.Autocomplete;
  originPlace!: google.maps.places.PlaceResult;

  @ViewChild('destinationAutocomplete')
  destinationAutocompleteInput!: ElementRef;
  destinationAutocompleter!: google.maps.places.Autocomplete;
  destinationPlace!: google.maps.places.PlaceResult;

  searchForm!: FormGroup;
  currentDate: string = new Date().toLocaleDateString('fr-ca');
  currentTime: string = new Date().toLocaleTimeString('it-it');
  departureArrivalFlag: boolean = true; //true for departure, false for arrival
  travelMode: RouteTravelMode = RouteTravelMode.TRANSIT;
  computeAlternativeRoutes: boolean = true;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private routeSearchSvc: RouteSearchService
  ) {}

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      origin: this.fb.control<string>('', [Validators.required]),
      destination: this.fb.control<string>('', [Validators.required]),
      date: this.fb.control<string>(this.currentDate),
      time: this.fb.control<string>(this.currentTime),
    });
  }

  ngAfterViewInit() {
    this.originAutocompleter = new google.maps.places.Autocomplete(
      this.originAutocompleteInput.nativeElement
    );
    this.destinationAutocompleter = new google.maps.places.Autocomplete(
      this.destinationAutocompleteInput.nativeElement
    );

    this.originAutocompleter.addListener('place_changed', () => {
      this.originPlace = this.originAutocompleter.getPlace();
      this.searchForm.get('origin')?.setValue(this.originPlace.name);
    });

    this.destinationAutocompleter.addListener('place_changed', () => {
      this.destinationPlace = this.destinationAutocompleter.getPlace();
      this.searchForm.get('destination')?.setValue(this.destinationPlace.name);
    });
  }

  submitForm() {
    this.router.navigate(['routes/results'], {
      queryParams: {
        origin: this.originPlace.place_id,
        originName: this.originPlace.name,
        destination: this.destinationPlace.place_id,
        destinationName: this.destinationPlace.name,
        time: this.routeSearchSvc.createISOString(
          this.searchForm.get('date')?.value,
          this.searchForm.get('time')?.value
        ),
        flag: this.departureArrivalFlag,
        travelMode: this.travelMode.valueOf(),
        computeAlternativeRoutes: this.computeAlternativeRoutes,
        //todo: add in travelmode, add in computeAlternativeRoutes
      },
    });
  }
}
