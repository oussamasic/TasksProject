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
import { Subscription } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
import { LoggerService } from 'src/app/service/logger.service';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrls: ['./task-create.component.scss'],
})
export class TaskCreateComponent implements OnInit, OnDestroy {
  public form: FormGroup;
  subscriptions: Subscription = new Subscription();
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private taskService: TaskService,
    private logger: LoggerService,
  ) {
    this.form = this.formBuilder.group({
      complete: false,
      description: [null, [Validators.required, Validators.minLength(10)]],
      title: [null, [Validators.required, Validators.minLength(5)]],
      startDate: [null],
      endDate: [null],
    });
  }

  ngOnDestroy() {
    this.subscriptions?.unsubscribe();
  }

  ngOnInit() {}

  newTask() {
    this.submitted = false;
  }

  createTask() {
    if (this.form.valid) {
      let taskDetails: Task = this.form.getRawValue();
      this.subscriptions.add(
        this.taskService.createTask(taskDetails).subscribe({
          next: () => {
            this.submitted = true;
            this.form.reset();
          },
          error: (error) => {
            this.submitted = false;
            this.logger.error('error : ', error);
          },
        }),
      );
    }
  }
}
