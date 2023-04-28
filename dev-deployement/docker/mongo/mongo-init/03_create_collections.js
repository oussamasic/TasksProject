db = db.getSiblingDB('manageTasksDB');

db.createCollection('tasks', {autoIndexId: true});