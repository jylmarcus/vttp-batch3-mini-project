import { Router } from '@angular/router';
import { AuthenticationService } from './../../../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{

  registrationForm!: FormGroup;
  registrationDetails: {username: string, password: string} = {username:'', password: ''};

  constructor(private authSvc: AuthenticationService, private router: Router, private fb: FormBuilder) {}

  ngOnInit(): void {
      this.registrationForm = this.fb.group( {
        username: this.fb.control<string>('', [Validators.required]),
        password: this.fb.control<string>('', [Validators.required])
      })
  }

  onLoginSubmit() {
    this.registrationDetails.username = this.registrationForm.value.username;
    this.registrationDetails.password = this.registrationForm.value.password;
    this.authSvc.register(this.registrationDetails).subscribe(
      {
        next: (data) => {
          this.router.navigate(['login']);
        },
        error: (e) => {
          this.authSvc.setAuthToken(null);
          alert(e.message);
        }
      }
    )
  }
}
