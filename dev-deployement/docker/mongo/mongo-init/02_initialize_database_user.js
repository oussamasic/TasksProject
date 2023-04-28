db = db.getSiblingDB('manageTasksDB');

// Create DB user

if (! db.getUser("manageTasksDB.user")) {
    db.createUser(
        {
            user: "crudUser",
            pwd: "oussama",
            roles: '[{ role: "readWrite", db: "manageTasksDB" }]'
        }
    )
}
else {
    db.updateUser(
        "manageTasksDB.user",
        {
            pwd: "oussama",
            roles: '[{ role: "readWrite", db: "manageTasksDB" }]'
        }
    )
}

