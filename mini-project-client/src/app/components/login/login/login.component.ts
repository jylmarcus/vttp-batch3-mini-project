import { Router } from '@angular/router';
import { AuthenticationService } from './../../../services/authentication.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatTabGroup } from '@angular/material/tabs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  @ViewChild('tabGroup') tabGroup!: MatTabGroup;

  loginForm!: FormGroup;
  loginDetails: {username: string, password: string} = {username: '', password: ''};
  registrationForm!: FormGroup;
  registrationDetails: {username: string, password: string} = {username:'', password: ''};

  constructor(private authSvc: AuthenticationService, private router: Router, private fb: FormBuilder, private dialogRef: MatDialogRef<LoginComponent>, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
      this.loginForm = this.fb.group( {
        username: this.fb.control<string>('', [Validators.required]),
        password: this.fb.control<string>('', [Validators.required])
      })

      this.registrationForm = this.fb.group( {
        username: this.fb.control<string>('', [Validators.required]),
        password: this.fb.control<string>('', [Validators.required])
      })
  }

  onLoginSubmit(event: Event): void {
    event?.preventDefault();
    this.loginDetails.username = this.loginForm.get('username')?.value;
    this.loginDetails.password = this.loginForm.get('password')?.value;
    this.authSvc.login(this.loginDetails).subscribe(
      {
        next: (data) => {
          this.authSvc.setAuthToken(data.token);
          this.router.navigate([''])
        },
        error: (e) => {
          this.authSvc.setAuthToken(null);
          this.snackBar.open(e.message, 'Close', {duration: 3000, horizontalPosition: 'center', verticalPosition: 'bottom'})
        }
      }
    )
    this.dialogRef.close();
  }

  onRegistrationSubmit(event: Event): void {
    event?.preventDefault();
    this.registrationDetails.username = this.registrationForm.value.username;
    this.registrationDetails.password = this.registrationForm.value.password;
    this.authSvc.register(this.registrationDetails).subscribe(
      {
        next: (data) => {
          this.tabGroup.selectedIndex = 0;
          this.snackBar.open('Registration Successful!', 'Close', {duration: 3000, horizontalPosition: 'center', verticalPosition: 'bottom'})
        },
        error: (e) => {
          this.authSvc.setAuthToken(null);
          this.snackBar.open(e.message, 'Close', {duration: 3000, horizontalPosition: 'center', verticalPosition: 'bottom'})
        }
      }
    )
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }
}
