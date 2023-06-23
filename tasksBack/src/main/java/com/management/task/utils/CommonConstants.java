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

package com.management.task.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {

    public static final String PATH_ID = "/{taskId}";
    public static final String TASKS = "/tasks";
    public static final String USERS = "/users";
    public static final String ALL_TASKS = "/allTasks";
    public static final String INCOMPLETE_TASKS = "/incompleteTasks";
    public static final String COMPLETE_TASKS = "/completeTasks";
}
