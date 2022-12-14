import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
import { TaskService } from 'src/app/service/task.service';
import { environment } from 'src/environments/environment';

import { TaskCreateComponent } from './task-create.component';

describe('TaskCreateComponent', () => {
  let component: TaskCreateComponent;
  let fixture: ComponentFixture<TaskCreateComponent>;

  const tasks: Task[] = [
    {
      id: '6393b96594534d02156bed9c',
      description: 'test te stes',
      complete: true,
    },
    {
      id: '6393b96e94534d02156bed9d',
      description: 'test test',
      complete: false,
    },
    {
      id: '6393c5608294ac24466b2a13',
      description: 'test test',
      complete: false,
    },
  ];

  const taskDetail: Task = {
    id: '6393c5608294ac24466b2a13',
    description: 'test test',
    complete: false,
  };

  const taskServiceMock = {
    getAllTasks: () => of(tasks),
    createTask: () => of(),
    getAllCompletedTasks: () =>
      of(tasks.filter((task) => task.complete === true)),
    deleteTask: () => of(),
    getTaskById: () => of(taskDetail),
    inCompleteTask: () => of(),
    completeTask: () => of(),
    getAllInCompletedTasks: () =>
      of(tasks.filter((task) => task.complete === false)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        FormBuilder,
        { provide: environment, useValue: environment },

        {
          provide: TaskService,
          useValue: taskServiceMock,
        },
      ],
      declarations: [TaskCreateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a valid form', () => {
    const expectedTask = {
      description: 'test test',
      complete: false,
    };
    component.form.setValue(expectedTask);
    expect(component.form.valid).toBeTruthy();
  });

  it('should have an invalid form', () => {
    const expectedTask = {
      description: 't',
      complete: false,
    };
    component.form.setValue(expectedTask);
    expect(component.form.valid).toBeFalsy();
  });

  it('should return false', () => {
    component.submitted = true;
    component.newTask();
    expect(component.submitted).toBeFalsy();
  });

  it('should call createTask of TaskService when form is valid', () => {
    // Given
    const expectedTask = {
      description: 'test test',
      complete: false,
    };
    component.form.setValue(expectedTask);
    spyOn(taskServiceMock, 'createTask').and.callThrough();

    // When
    component.createTask();

    // Then
    expect(taskServiceMock.createTask).toHaveBeenCalled();
  });

  it('should not call createTask of TaskService when form is invalid', () => {
    // Given
    const expectedTask = {
      description: 't',
      complete: false,
    };
    component.form.setValue(expectedTask);
    spyOn(taskServiceMock, 'createTask').and.callThrough();

    // When
    component.createTask();

    // Then
    expect(taskServiceMock.createTask).not.toHaveBeenCalled();
  });
});
