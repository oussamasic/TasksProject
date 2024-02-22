/*
 * <OZ TASKS>
 * <project to manage user tasks>
 * Copyright (C) <2023>  <ZEROUALI Oussama>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { LoggerService } from '../service/logger.service';
import { LoginLgoutUserService } from '../service/login-lgout-user.service';

@Injectable({
  providedIn: 'root',
})
export class TokenInterceptorService implements HttpInterceptor {
  constructor(
    private logoutUser: LoginLgoutUserService,
    private logger: LoggerService,
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var userConnected = JSON.parse(localStorage.getItem('userConnected'));
    if (userConnected) {
      var token = userConnected.token;
      if (token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }

      localStorage.setItem(
        'userConnected',
        JSON.stringify({ token: userConnected.token, email: userConnected.email, updatedDate: new Date() }),
      );
    }

    return next.handle(request).pipe(
      catchError((err) => {
        if (err.status === 401) {
          this.logoutUser.logoutUser();
        }
        if (err.status === 403) {
          this.logger.error('error 403 message ', err.message);
        }
        if (err.status === 500) {
          this.logger.error('error 500 message ', err.message);
        }
        if (err.status === 404) {
          this.logger.error('error 404 message ', err.message);
        }
        const error = err.error.message || err.statusText;
        return throwError(error);
      }),
    );
  }
}
