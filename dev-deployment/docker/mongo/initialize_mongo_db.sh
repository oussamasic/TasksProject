
echo "Execute scripts to initialize the database"

mongosh --shell init-mongodb/01_initialize_database_user.js
mongosh --shell init-mongodb/02_create_collections.js

echo "Mongo service is started and the database is initialized"