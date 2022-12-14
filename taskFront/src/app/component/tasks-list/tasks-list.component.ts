import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-tasks-list',
  templateUrl: './tasks-list.component.html',
  styleUrls: ['./tasks-list.component.scss'],
})
export class TasksListComponent implements OnInit, OnDestroy {
  tasks: Task[];
  subscription: Subscription = new Subscription();
  constructor(private service: TaskService) {}

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.getAllTasks();
  }

  getTasksStatus(complete: boolean): string {
    return complete ? 'COMPLETED' : 'IN PROGRESS';
  }

  deleteTask(id: string): void {
    this.subscription.add(
      this.service.deleteTask(id).subscribe(
        () => {
          this.getAllTasks();
        },
        (error) => {
          console.log('error :', error);
        }
      )
    );
  }

  inCompleteTask(id: string): void {
    this.subscription.add(
      this.service.inCompleteTask(id).subscribe(
        () => {
          this.getAllTasks();
        },
        (error) => {
          console.log('error :', error);
        }
      )
    );
  }
  completeTask(id: string): void {
    this.subscription.add(
      this.service.completeTask(id).subscribe(
        () => {
          this.getAllTasks();
        },
        (error) => {
          console.log('error :', error);
        }
      )
    );
  }

  getAllCompletedTasks() {
    this.subscription.add(
      this.service.getAllCompletedTasks().subscribe(
        (data) => {
          this.tasks = data;
        },
        (error) => {
          console.log('error while getting tasks from Back server', error);
        }
      )
    );
  }
  getAllIncomletedTaks() {
    this.subscription.add(
      this.service.getAllInCompletedTasks().subscribe(
        (data) => {
          this.tasks = data;
        },
        (error) => {
          console.log('error while getting tasks from Back server', error);
        }
      )
    );
  }

  getAllTasks(): void {
    this.subscription.add(
      this.service.getAllTasks().subscribe(
        (data) => {
          this.tasks = data;
        },
        (error) => {
          console.log('error while getting tasks from Back server', error);
        }
      )
    );
  }
}
