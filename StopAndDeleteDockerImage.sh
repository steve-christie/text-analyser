docker rm $(docker stop $(docker ps -a -q --filter ancestor=text_analyser --format="{{.ID}}"))
docker ps
