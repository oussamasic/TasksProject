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
    return this.http.get<Task[]>(this.baseUrl + '/allTasks');
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
