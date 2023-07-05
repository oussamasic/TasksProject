#docker stop tasks-mongo
docker-compose -f ./mongo_tasks.yml down --remove-orphans

echo "tasks-mongo is stopped"