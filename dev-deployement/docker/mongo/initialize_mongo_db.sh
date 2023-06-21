
echo "Execute scripts to initialize the database"

mongo --shell init-mongodb/01_initialize_database_user.js
mongo --shell init-mongodb/02_create_collections.js

echo "Mongod service is started and the database is initialized"