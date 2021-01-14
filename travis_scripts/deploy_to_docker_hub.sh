echo "Pushing service docker images to docker hub ...."
docker login -u $DOCKER_NAME -p $DOCKER_PASSWORD
docker push ngomalalibo/confserver
docker push ngomalalibo/authservice
docker push ngomalalibo/eurekaserver
docker push ngomalalibo/stockquote
docker push ngomalalibo/zuulserver
