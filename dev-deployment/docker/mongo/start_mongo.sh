docker-compose -f ./mongo_tasks.yml up -d

echo "Execute scripts to initialize the database"

 mongo --shell mongo-init.js

echo "Mongod service is started and the database is initialized"