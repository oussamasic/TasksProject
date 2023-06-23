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

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskApiService } from '../api/task-api.service';
import { Task } from '../model/task.interface';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private taskApiService: TaskApiService) {}

  getAllTasks(): Observable<Task[]> {
    return this.taskApiService.getAll();
  }

  deleteTask(id: string): Observable<void> {
    return this.taskApiService.deleteTask(id);
  }
  completeTask(id: string): Observable<void> {
    return this.taskApiService.completeTask(id);
  }

  createTask(task: Task): Observable<void> {
    return this.taskApiService.createTask(task);
  }
  inCompleteTask(id: string): Observable<void> {
    return this.taskApiService.inCompleteTask(id);
  }

  getAllCompletedTasks(): Observable<Task[]> {
    return this.taskApiService.getAllCompletedTasks();
  }

  getAllInCompletedTasks(): Observable<Task[]> {
    return this.taskApiService.getAllInCompletedTasks();
  }

  getTaskById(id: string): Observable<Task> {
    return this.taskApiService.getTaskById(id);
  }
}
