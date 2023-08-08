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

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { TaskApiService } from '../api/task-api.service';
import { Task } from '../model/task.interface';

import { TaskService } from './task.service';

describe('TaskService', () => {
  let service: TaskService;

  const taskApiServiceMock = {
    getAll: () => of([]),
    getAllCompletedTasks: () => of([]),
    getAllInCompletedTasks: () => of([]),
    inCompleteTask: () => of(),
    getTaskById: () => of({}),
    deleteTask: () => of(),
    completeTask: () => of(),
    createTask: () => of(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: TaskApiService, useValue: taskApiServiceMock }],
    });
    service = TestBed.inject(TaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getAll of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'getAll').and.callThrough();

    // When
    service.getAllTasks();

    // Then
    expect(taskApiServiceMock.getAll).toHaveBeenCalled();
  });

  it('should call getAllTasks of TaskApiService when we search completed tasks', () => {
    // Given
    spyOn(taskApiServiceMock, 'getAll').and.callThrough();

    // When
    service.getAllTasks('complete');

    // Then
    expect(taskApiServiceMock.getAll).toHaveBeenCalled();
  });

  it('should call getAllTasks of TaskApiService when we search incompleted tasks', () => {
    // Given
    spyOn(taskApiServiceMock, 'getAll').and.callThrough();

    // When
    service.getAllTasks('incomplete');

    // Then
    expect(taskApiServiceMock.getAll).toHaveBeenCalled();
  });

  it('should call inCompleteTask of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'inCompleteTask').and.callThrough();

    // When
    service.inCompleteTask('taskId');

    // Then
    expect(taskApiServiceMock.inCompleteTask).toHaveBeenCalled();
  });

  it('should call getTaskById of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'getTaskById').and.callThrough();

    // When
    service.getTaskById('taskId');

    // Then
    expect(taskApiServiceMock.getTaskById).toHaveBeenCalled();
  });

  it('should call createTask of TaskApiService', () => {
    // Given
    const task: Task = {
      id: 'id',
      description: 'description',
      complete: false,
      userId: 'userId',
      title: 'taskTitle',
      creationDate: new Date(),
      startDate: new Date(),
      endDate: new Date(),
    };
    spyOn(taskApiServiceMock, 'createTask').and.callThrough();

    // When
    service.createTask(task);

    // Then
    expect(taskApiServiceMock.createTask).toHaveBeenCalled();
  });

  it('should call completeTask of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'completeTask').and.callThrough();

    // When
    service.completeTask('id');

    // Then
    expect(taskApiServiceMock.completeTask).toHaveBeenCalled();
  });
});
