import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit{

  constructor(private authSvc: AuthenticationService, private router: Router){}

  ngOnInit(): void {
      this.authSvc.setAuthToken(null);
      this.router.navigate(['login']);
  }
}
