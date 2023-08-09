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
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
import { TaskService } from 'src/app/service/task.service';
import { environment } from 'src/environments/environment';

import { TasksListComponent } from './tasks-list.component';

describe('TasksListComponent', () => {
  let component: TasksListComponent;
  let fixture: ComponentFixture<TasksListComponent>;

  const tasks: Task[] = [
    {
      id: '6393b96594534d02156bed9c',
      description: 'test te stes',
      complete: true,
      userId: 'userId',
      title: 'taskTitle',
      creationDate: new Date(),
      startDate: new Date(),
      endDate: new Date(),
    },
    {
      id: '6393b96e94534d02156bed9d',
      description: 'test test',
      complete: false,
      userId: 'userId',
      title: 'taskTitle',
      creationDate: new Date(),
      startDate: new Date(),
      endDate: new Date(),
    },
    {
      id: '6393c5608294ac24466b2a13',
      description: 'test test',
      complete: false,
      userId: 'userId',
      title: 'taskTitle',
      creationDate: new Date(),
      startDate: new Date(),
      endDate: new Date(),
    },
  ];

  const taskServiceMock = {
    getAllTasks: () => of(tasks),
    getAllCompletedTasks: () => of(tasks.filter((task) => task.complete === true)),
    deleteTask: () => of(),
    inCompleteTask: () => of(),
    completeTask: () => of(),
    getAllInCompletedTasks: () => of(tasks.filter((task) => task.complete === false)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [TasksListComponent],
      providers: [
        { provide: environment, useValue: environment },
        {
          provide: TaskService,
          useValue: taskServiceMock,
        },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getAllTasks of TaskService when we search all completed tasks', () => {
    // Given
    spyOn(taskServiceMock, 'getAllTasks').and.callThrough();

    // When
    component.getAllCompletedTasks();

    // Then
    expect(taskServiceMock.getAllTasks).toHaveBeenCalled();
  });

  it('should return completed', () => {
    const response = component.getTasksStatus(true);
    expect(response).not.toBeNull();
    expect(response).toEqual('COMPLETED');
  });

  it('should call getAllTasks of TaskService when we search all incompleted tasks', () => {
    // Given
    spyOn(taskServiceMock, 'getAllTasks').and.callThrough();

    // When
    component.getAllIncomletedTaks();

    // Then
    expect(taskServiceMock.getAllTasks).toHaveBeenCalled();
  });

  it('should return in progress', () => {
    const response = component.getTasksStatus(false);
    expect(response).not.toBeNull();
    expect(response).toEqual('IN PROGRESS');
  });

  it('should call getAllTasks of TaskService', () => {
    // Given
    spyOn(taskServiceMock, 'getAllTasks').and.callThrough();

    // When
    component.getAllTasks();

    // Then
    expect(taskServiceMock.getAllTasks).toHaveBeenCalled();
  });

  it('should have one title', () => {
    const nativeElement = fixture.nativeElement;
    const titleElement = nativeElement.querySelectorAll('h2');
    expect(titleElement.length).toBe(1);
  });

  it('should call createTask of TaskService', () => {
    // Given
    spyOn(taskServiceMock, 'deleteTask').and.callThrough();

    // When
    component.deleteTask('taskId');

    // Then
    expect(taskServiceMock.deleteTask).toHaveBeenCalled();
  });

  it('should have Taks Details as title ', () => {
    const nativeElement = fixture.nativeElement;
    const elementVitamuiInput = nativeElement.querySelector('h2');
    expect(elementVitamuiInput.textContent).toContain('Taks Details');
  });

  it('should call completeTask of TaskService', () => {
    // Given
    spyOn(taskServiceMock, 'completeTask').and.callThrough();

    // When
    component.completeTask('taskId');

    // Then
    expect(taskServiceMock.completeTask).toHaveBeenCalled();
  });

  it('should call inCompleteTask of TaskService', () => {
    // Given
    spyOn(taskServiceMock, 'inCompleteTask').and.callThrough();

    // When
    component.inCompleteTask('taskId');

    // Then
    expect(taskServiceMock.inCompleteTask).toHaveBeenCalled();
  });
});
