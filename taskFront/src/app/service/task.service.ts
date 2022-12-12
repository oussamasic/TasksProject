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
