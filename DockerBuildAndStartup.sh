mvn install
docker build --no-cache -t text-analyser .
docker run -p 8080:8080 -d text-analyser:latest
docker ps

