import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export const authGuard: CanActivateFn = (route, state) => {

  let snackBar= inject(MatSnackBar);
  let authSvc = inject(AuthenticationService);
  let router = inject(Router);

  if(authSvc.getAuthToken()) {
    return true;
  } else {
    router.navigate(['']);
    snackBar.open('Please login before trying out our features', 'Dismiss', {duration: 3000});
  }
  return false;
};
