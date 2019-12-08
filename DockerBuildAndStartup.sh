mvn install
docker build --no-cache -t text_analyser .
docker run -p 8080:8080 -d text_analyser:latest


docker ps

