import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  public getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

  public setAuthToken(token: string | null): void {
    if(token !== null) {
      window.localStorage.setItem("auth_token", token);
    } else {
      window.localStorage.removeItem("auth_token");
    }
  }

  public login(input: any): Observable<any> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    this.addAuthorizationHeader(headers);
    return this.http.post<any>("/api/auth/login", input, {headers: headers});
    //.subscribe(
    //  (response) => {
    //    this.setAuthToken(response.data.token);
    //router navigate
    //  error => { this.setAuthToken(null); router.navigate welcome}
    //  }
    //);
  }

  public register(input: any): Observable<any> {
    const headers = new HttpHeaders().set('content-type', 'application/json');
    this.addAuthorizationHeader(headers);
    return this.http.post<any>("/api/auth/register", input, {headers: headers});
  }

  public addAuthorizationHeader(headers: HttpHeaders) {
    if(this.getAuthToken() !== null) {
      headers.set('Authorization', 'Bearer ' + this.getAuthToken());
    }
  }
}
