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

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginLgoutUserService } from './service/login-lgout-user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  constructor(
    private router: Router,
    private loginLogoutUser: LoginLgoutUserService,
  ) {}

  title = 'Management Tasks Application';

  ngOnInit() {
    var userConnected = JSON.parse(localStorage.getItem('userConnected'));
    if (userConnected) {
      console.log('date ', userConnected.updatedDate);

      if (new Date().getTime() - userConnected.updatedDate.getTime() > 60 * 60 * 1000) {
        this.logoutUser();
      }
    }
  }

  logoutUser() {
    this.loginLogoutUser.logoutUser().subscribe(() => {
      localStorage.removeItem('userConnected');
      this.router.navigate(['login']);
    });
  }
}
