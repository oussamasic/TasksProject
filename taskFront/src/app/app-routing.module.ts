import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskCreateComponent } from './component/task-create/task-create.component';
import { TaskDetailsComponent } from './component/task-details/task-details.component';
import { TasksListComponent } from './component/tasks-list/tasks-list.component';

// const routes: Routes = [];

const routes: Routes = [
  { path: 'addTask', component: TaskCreateComponent },
  { path: 'tasks', component: TasksListComponent },
  { path: 'tasks/:id', component: TaskDetailsComponent },
  // { path: 'search', component: SearchPersonComponent },
];

// {
//   path: 'transactions/:projectId',
//   loadChildren: () => import('./transactions/transaction.module').then((m) => m.TransactionModule),
//   canActivate: [AppGuard],
// },

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
