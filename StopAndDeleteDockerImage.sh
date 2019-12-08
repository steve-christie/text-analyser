docker rm $(docker stop $(docker ps -a -q --filter ancestor=text-analyser --format="{{.ID}}"))
docker ps
