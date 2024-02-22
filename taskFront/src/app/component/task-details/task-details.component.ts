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
import { ActivatedRoute } from '@angular/router';
import { mergeMap } from 'rxjs/operators';
import { Task } from 'src/app/model/task.interface';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.scss'],
})
export class TaskDetailsComponent implements OnInit {
  task: Task;
  taskId: string;
  taskFound = false;

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService,
  ) {}

  ngOnInit(): void {
    this.route.params
      .pipe(
        mergeMap((params) => {
          this.taskId = params.id;
          return this.taskService.getTaskById(params.id);
        }),
      )
      .subscribe({
        next: (task) => {
          this.taskFound = true;
          this.task = task;
        },
        error: (error) => {
          this.taskFound = false;
          console.log('error', error);
        },
      });
  }

  getTasksStatus(complete: boolean): string {
    return complete ? 'COMPLETED' : 'IN PROGRESS';
  }

  showUserDetails(userId: string) {
    console.log('test :', userId);
  }
}
