import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { LoginComponent } from '../../login/login/login.component';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{

  isLoggedIn!: boolean;

  constructor(private authSvc: AuthenticationService, private router: Router, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.authSvc.authToken$.subscribe(token => {
      this.isLoggedIn = !!token;
    })
  }

  openLoginDialog(): void {
    const dialogRef = this.dialog.open(LoginComponent, {width: '40%', height: '40%'})
  }

  logout() {
    this.authSvc.setAuthToken(null);
    this.router.navigate(['']);
  }
}
