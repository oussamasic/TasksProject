print("START 01_initialize_database_user.js");

const conn = new Mongo();
db = conn.getDB("manageTasksDB");

// Create the DB admin user

if (db.getUser("crudUser")== null) {

print("Create new database admin user");
    db.createUser(
        {
            user: "crudUser",
            pwd: "oussama",
            roles: [{ role: "readWrite", db: "manageTasksDB" }]
        }
    )
}

// Update the DB admin user

else {

print("Update the database admin user");
    db.updateUser(
        "crudUser",
        {
            pwd: "oussama",
            roles: [{ role: "readWrite", db: "manageTasksDB" }]
        }
    )
}

print("END 01_initialize_database_user.js");

quit()

