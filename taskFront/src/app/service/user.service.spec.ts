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
import { UserApiService } from '../api/user-api.service';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;

  const userApiServiceMock = {
    downloadUserTasksReportNormal: () => of(),
    findUserByEmail: () => of({}),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: UserApiService, useValue: userApiServiceMock }],
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call findUserByEmail of UserApiService', () => {
    // Given
    spyOn(userApiServiceMock, 'findUserByEmail').and.callThrough();

    // When
    service.findUserByEmail('email');

    // Then
    expect(userApiServiceMock.findUserByEmail).toHaveBeenCalled();
  });

  it('should call downloadUserTasksReportNormal of UserApiService', () => {
    // Given
    spyOn(userApiServiceMock, 'downloadUserTasksReportNormal').and.callThrough();

    // When
    service.downloadUserTasksReportNormal('email');

    // Then
    expect(userApiServiceMock.downloadUserTasksReportNormal).toHaveBeenCalled();
  });
});
