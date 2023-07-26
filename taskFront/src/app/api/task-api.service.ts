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

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../model/task.interface';

@Injectable({
  providedIn: 'root',
})
export class TaskApiService {
  private baseUrl = '/api/tasks';

  constructor(private http: HttpClient) {}

  public getAll(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl + '/all');
  }

  public deleteTask(taskId: string): Observable<void> {
    return this.http.delete<void>(this.baseUrl + '/' + taskId + '/delete', {});
  }

  public completeTask(taskId: string): Observable<void> {
    return this.http.put<void>(this.baseUrl + '/' + taskId + '/complete', {});
  }

  public inCompleteTask(taskId: string): Observable<void> {
    return this.http.put<void>(this.baseUrl + '/' + taskId + '/incomplete', {});
  }

  public getAllCompletedTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl + '/completeTasks');
  }
  public getAllInCompletedTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl + '/incompleteTasks');
  }

  public createTask(task: Task): Observable<void> {
    return this.http.post<void>(this.baseUrl, task, {});
  }

  public getTaskById(id: string): Observable<Task> {
    return this.http.get<Task>(this.baseUrl + '/' + id);
  }
}
