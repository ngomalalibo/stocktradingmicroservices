echo "Launching $BUILD_NAME IN AMAZON ECS"
docker container rm -f $(docker container ls -aq)
docker image rm -f $(docker image ls -aq)
docker-compose up -d
docker-compose ps -a
