import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Task } from 'src/app/model/task.interface';
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
    private taskService: TaskService
  ) {
    this.form = this.formBuilder.group({
      complete: false,
      description: ['', [Validators.required, Validators.minLength(5)]],
    });
  }
  ngOnDestroy(): void {
    this.subscriptions?.unsubscribe();
  }

  ngOnInit(): void {}

  newTask() {
    this.submitted = false;
  }

  createTask() {
    if (this.form.valid) {
      const taskDetails: Task = this.form.getRawValue();
      this.subscriptions.add(
        this.taskService.createTask(taskDetails).subscribe(
          () => {
            this.submitted = true;
            this.form.reset();
          },
          (error) => {
            this.submitted = false;
          }
        )
      );
    }
  }
}
