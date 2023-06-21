print("START 02_create_collections.js");

const conn = new Mongo();
db = conn.getDB("manageTasksDB");

const collection_names = db.getCollectionNames();

// ----- create tasks collection
if(collection_names.includes("tasks")) {
    print("collection tasks already exists");
} else {
db.createCollection('tasks', {autoIndexId: true});
}

// ----- create users collection
if(collection_names.includes("users")) {
    print("collection users already exists");
} else {
db.createCollection('users', {autoIndexId: true});
}

// ----- create tokens collection
if(collection_names.includes("tokens")) {
    print("collection tokens already exists");
} else {
db.createCollection('tokens', {autoIndexId: true});
}

print("END 02_create_collections.js");
quit();