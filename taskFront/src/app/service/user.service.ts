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

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserApiService } from '../api/user-api.service';
import { User } from '../model/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private userApiService: UserApiService) {}

  downloadFile(url: string) {
    window.addEventListener('focus', window_focus, false);

    function window_focus() {
      window.removeEventListener('focus', window_focus, false);
      URL.revokeObjectURL(url);
    }

    location.href = url;
  }

  downloadUserTasksReport(userId: string) {
    this.downloadFile(this.userApiService.downloadUserTasksReport(userId));
  }

  downloadUserTasksReportNormal(userID: string) {
    return this.userApiService.downloadUserTasksReportNormal(userID).subscribe((file) => {
      const element = document.createElement('a');
      element.href = window.URL.createObjectURL(file);
      element.download = 'Report-' + userID + '.pdf';
      element.style.visibility = 'hidden';
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    });
  }

  findUserByEmail(userEmail: string): Observable<User> {
    return this.userApiService.findUserByEmail(userEmail);
  }
}
