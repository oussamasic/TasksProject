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
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
import { TaskService } from 'src/app/service/task.service';
import { environment } from 'src/environments/environment';

import { TaskDetailsComponent } from './task-details.component';

describe('TaskDetailsComponent', () => {
  let component: TaskDetailsComponent;
  let fixture: ComponentFixture<TaskDetailsComponent>;

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
      userId: 'userId',
      title: 'taskTitle',
      creationDate: new Date(),
      startDate: new Date(),
      endDate: new Date(),
      id: '6393c5608294ac24466b2a13',
      description: 'test test',
      complete: false,
    },
  ];

  const taskDetail: Task = {
    id: '6393c5608294ac24466b2a13',
    description: 'test test',
    userId: 'userId',
    title: 'taskTitle',
    creationDate: new Date(),
    startDate: new Date(),
    endDate: new Date(),
    complete: false,
  };

  const taskServiceMock = {
    getAllTasks: () => of(tasks),
    getAllCompletedTasks: () => of(tasks.filter((task) => task.complete === true)),
    deleteTask: () => of(),
    getTaskById: () => of(taskDetail),
    inCompleteTask: () => of(),
    completeTask: () => of(),
    getAllInCompletedTasks: () => of(tasks.filter((task) => task.complete === false)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [TaskDetailsComponent],
      providers: [
        { provide: environment, useValue: environment },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: 1 }),
          },
        },
        {
          provide: TaskService,
          useValue: taskServiceMock,
        },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return in progress', () => {
    const response = component.getTasksStatus(false);
    expect(response).not.toBeNull();
    expect(response).toEqual('IN PROGRESS');
  });

  it('should return completed', () => {
    const response = component.getTasksStatus(true);
    expect(response).not.toBeNull();
    expect(response).toEqual('COMPLETED');
  });
});
