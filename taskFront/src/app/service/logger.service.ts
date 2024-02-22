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

@Injectable({
  providedIn: 'root',
})
export class LoggerService {
  info(parentClass: Object, ...msg: any) {
    const className = parentClass.constructor.name;
    console.info('%c' + new Date().toLocaleString() + ' %c' + className, 'color: red', 'color: blue', ...msg);
  }
  log(parentClass: Object, ...msg: any) {
    const className = parentClass.constructor.name;
    console.log('%c' + new Date().toLocaleString() + ' %c' + className, 'color: red', 'color: blue', ...msg);
  }

  warn(parentClass: Object, ...msg: any) {
    const className = parentClass.constructor.name;
    console.warn('%c' + new Date().toLocaleString() + ' %c' + className, 'color: red', 'color: blue', ...msg);
  }

  error(parentClass: Object, ...msg: any) {
    const className = parentClass.constructor.name;
    console.error('%c' + new Date().toLocaleString() + ' %c' + className, 'color: red', 'color: blue', ...msg);
  }
}
