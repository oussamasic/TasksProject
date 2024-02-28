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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user.interface';
import { LoggerService } from 'src/app/service/logger.service';
import { LoginLgoutUserService } from 'src/app/service/login-lgout-user.service';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.scss'],
})
export class LoginUserComponent implements OnInit, OnDestroy {
  public loginForm: FormGroup;
  private loginSubscription: Subscription = new Subscription();

  constructor(
    private loginLogoutUserService: LoginLgoutUserService,
    private formBuilder: FormBuilder,
    private router: Router,
    private logger: LoggerService,
  ) {
    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, Validators.required],
    });
  }

  ngOnInit() {}

  ngOnDestroy() {
    this.loginSubscription.unsubscribe();
  }

  loginUser() {
    const user: User = this.loginForm.getRawValue();

    this.loginSubscription = this.loginLogoutUserService.loginUser(user).subscribe({
      next: (data) => {
        localStorage.setItem('userConnected', JSON.stringify({ token: data, email: user.email, updatedDate: new Date() }));
        this.router.navigate(['tasks']);
      },
      error: (error) => {
        this.logger.error('the error is : ', error);
      },
    });
  }
}
