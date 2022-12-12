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

  it('should call getAllCompletedTasks of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'getAllCompletedTasks').and.callThrough();

    // When
    service.getAllCompletedTasks();

    // Then
    expect(taskApiServiceMock.getAllCompletedTasks).toHaveBeenCalled();
  });

  it('should call getAllInCompletedTasks of TaskApiService', () => {
    // Given
    spyOn(taskApiServiceMock, 'getAllInCompletedTasks').and.callThrough();

    // When
    service.getAllInCompletedTasks();

    // Then
    expect(taskApiServiceMock.getAllInCompletedTasks).toHaveBeenCalled();
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
