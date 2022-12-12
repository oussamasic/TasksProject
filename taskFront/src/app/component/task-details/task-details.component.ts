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
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.route.params
      .pipe(
        mergeMap((params) => {
          this.taskId = params.id;
          return this.taskService.getTaskById(params.id);
        })
      )
      .subscribe(
        (task) => {
          this.taskFound = true;
          this.task = task;
        },
        (error) => {
          this.taskFound = false;
          console.log('error', error);
        }
      );
  }

  getTasksStatus(complete: boolean): string {
    return complete ? 'COMPLETED' : 'IN PROGRESS';
  }
}
