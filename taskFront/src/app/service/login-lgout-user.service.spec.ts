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

import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { LoginLgoutUserApiService } from '../api/login-lgout-user-api.service';
import { User } from '../model/user.interface';
import { LoginLgoutUserService } from './login-lgout-user.service';

describe('LoginLgoutUserService', () => {
  let service: LoginLgoutUserService;

  const loginLgoutUserApiServiceMock = {
    loginUser: () => of('tokenUser'),
    logoutUser: () => of(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: LoginLgoutUserApiService,
          useValue: loginLgoutUserApiServiceMock,
        },
      ],
    });
    service = TestBed.inject(LoginLgoutUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call loginUser of LoginLgoutUserApiService', () => {
    // Given
    const user = { email: 'email@email.com', password: 'password' } as User;
    spyOn(loginLgoutUserApiServiceMock, 'loginUser').and.callThrough();

    // When
    service.loginUser(user);

    // Then
    expect(loginLgoutUserApiServiceMock.loginUser).toHaveBeenCalled();
  });

  it('should call logoutUser of LoginLgoutUserApiService', () => {
    // Given
    spyOn(loginLgoutUserApiServiceMock, 'logoutUser').and.callThrough();

    // When
    service.logoutUser();

    // Then
    expect(loginLgoutUserApiServiceMock.logoutUser).toHaveBeenCalled();
  });
});
