db = db.getSiblingDB('manageTasksDB');

db.createCollection('tasks', {autoIndexId: true});
db.createCollection('users', {autoIndexId: true});
db.createCollection('tokens', {autoIndexId: true});