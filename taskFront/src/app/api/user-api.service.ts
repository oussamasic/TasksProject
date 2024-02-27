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

import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserApiService {
  private baseUrl = '/api/users';

  constructor(private http: HttpClient) {}

  downloadUserTasksReportNormal(userId: string): Observable<Blob> {
    return this.http.get(this.baseUrl + '/' + userId + '/tasks/report', { responseType: 'blob' });
  }

  downloadUserTasksReport(userId: string): string {
    return this.baseUrl + '/' + userId + '/tasks/report';
  }

  findUserByEmail(userEmail: string): Observable<User> {
    return this.http.get<User>(this.baseUrl + '?email=' + userEmail);
  }

  downloadUserTasksWebFluxReport(): Observable<HttpResponse<Blob>> {
    return this.http.get(this.baseUrl + '/tasks/web-flux-report', {
      observe: 'response',
      responseType: 'blob',
    });
  }
}
