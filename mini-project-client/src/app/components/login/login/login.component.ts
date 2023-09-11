import { Router } from '@angular/router';
import { AuthenticationService } from './../../../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  loginForm!: FormGroup;
  loginDetails: {username: string, password: string} = {username: '', password: ''};

  constructor(private authSvc: AuthenticationService, private router: Router, private fb: FormBuilder) {}

  ngOnInit(): void {
      this.loginForm = this.fb.group( {
        username: this.fb.control<string>('', [Validators.required]),
        password: this.fb.control<string>('', [Validators.required])
      })
  }

  onLoginSubmit() {
    this.loginDetails.username = this.loginForm.get('username')?.value;
    this.loginDetails.password = this.loginForm.get('password')?.value;
    this.authSvc.login(this.loginDetails).subscribe(
      {
        next: (data) => {
          this.authSvc.setAuthToken(data.token);
          this.router.navigate(['routes/search'])
        },
        error: (e) => {
          this.authSvc.setAuthToken(null);
          alert(e.message);
        }
      }
    )
  }

  goToRegistration() {
    this.router.navigate(['register'])
  }
}
