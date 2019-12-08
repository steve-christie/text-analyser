# Text Analyser
This service is designed to take input in the form of a string or the path to a file. The output will inform the 
user on some meta data about the input, notably the number of words and the average word length. The service is
accessed via a REST API.

## Api
The API is available at the following paths:
* POST: `/analyser/string`
* POST: `/analyser/file`

### Inputs:
Both paths accept JSON as their input, with the keys differing slightly for each.

##### /string
```json
{
  "text": "sample text here"
}
```

##### /file
```json
{
  "filePath":"/Users/steven.christie/Development/Personal/services/text-analyzer/src/test/resources/bible_3daily.txt"
}
```

### Output:
The output of both paths will always follow the same format:

```
Word Count = 9
Average Word Length = 4.556
Number of words of length 1 is 1
Number of words of length 2 is 1
Number of words of length 3 is 1
Number of words of length 4 is 2
Number of words of length 5 is 2
Number of words of length 7 is 1
Number of words of length 10 is 1
The most frequently occurring word length is 2, for word lengths of 4 & 5
```

## Making Requests

As mentioned earlier, the service offers a REST API in order to communicate with it. Here are some sample CURL requests
demonstrating how the API can be used via command line:

##### Posting to /text_analyser/string 
```
curl -X POST \
  http://localhost:8080/analyser/string \
  -H 'Content-Type: application/json' \
  -d '{"text": "sample text value"}'
```

##### Posting to /text_analyser/file 
```
curl -X POST \
  http://localhost:9006/analyser/file \
  -H 'Content-Type: application/json' \
  -d '{"filePath":"/root/to/file/file.txt"}'
```

### Postman
If your a fan of the [Postman](https://www.getpostman.com/) application for managing API calls, a collection is 
available within the root of the project that can be imported into Postman and set up the necessary calls straight away.
All you'll need to do is start the application and ensure the port in the URL matches what the service has spun 
up on (see below for more detail).

## Starting The Service

There are a few options available to start the service with a few listed below. The repository contains files
to enable docker usage, so if you have docker installed, this is likely the quickest route to get it up and running.

Note. the service by default will run on port 8080. If this will cause conflict on your machine, 
there are notes below on how you can change the port used.

### Via IntelliJ

1. Unzip or download the project into a directory of your choosing
2. From IntelliJ, choose File > New > Project From Existing Sources
3. Navigate to the pom.xml in the project directory and open from here

If Intellij is not configured to automatically download all import sources, ensure this is done manually.

You will also need to download the Lombok plugin as well as turn on the 'Enable Annotation Processing' setting found
under 'Build, Execution, Deployment > Compiler > Annotation Processors'

The service can be started by finding the 'TextAnalyserApplication' class in the project directory, righting clicking 
and choosing the 'Run' option. Alternatively, a run configuration can be set up via Run > Edit Configurations.

Intellij Community Edition
1. Click the + icon
2. Choose the 'Application' type
3. Choose an appropriate name for the config (eg. Text Analyser)
4. Set the Main Class to 'TextAnalyserApplication'
5. Save

After doing so, a configuration with the name provided should now be available in your run configurations. Select this
and click either the 'Run' or 'Debug' button to start the application.

IntelliJ Ultimate Edition
Repeat steps 1 -> 5 above, however you can select 'SpringBoot Application' instead of 'Application'

### Via Docker
A couple of scripts are provided in the root of the project to quickly spin up the service with docker. These will
require the machine you are on to be running Docker. 

To build and start the application from command line, you can use:
```DockerBuildAndStartup.sh```

To stop the service and ensure the docker image is removed, you can use: 
```StopAndDeleteDockerImage.sh```

### Via Docker Compose
A docker-compose file is also included in the repo if your preference is to run via this method. The following 
commands will build the image and start it up:
```
mvn install
docker-compose build --no-cache
docker-compose up -d
```
To bring the service down afterwards run the following:
```
docker-compose down
```

### Analysing Files when the application has been spun up with Docker
If you've spun up the application using docker and have attempted to analyse a file you'll likely have seen an error
telling you the file could not be located, but you're sure the path to the file is correct. 

Don't panic!

This is because docker has its own file system and the file you're wanting to test doesnt exist there. Not to worry,
both docker options have the ability to copy over files from a folder in the root of the project directory called 
'sample_data'. Files placed in this directory will be copied over when the docker container is started and can then be
used. For both docker options, files will be placed in the '/var/sample_data/" directory inside the container.

The 'sample_data' folder has been preloaded with a 'sample_file.txt' file, so when the docker container is spun up,
this will be available in "/var/sample_data/sample_file.txt", so doing the following should return a result:
```
curl -X POST \
  http://localhost:8080/analyser/file \
  -H 'Content-Type: application/json' \
  -d '{
	"filePath":"/var/sample_data/sample_file.txt"
}
```

### Changing The Port

When spinning up via Intellij, this can be done by either setting an Environment Variable called 'SERVER_PORT' with
a port number you'd like to use, or by changing the port set in the application.yml file found under src > main > 
resources > application.yml

If running via the scripts provided, you will need to make the same change as above to the application.yml file,
but you will also need to edit the 'Dockerfile' in the project directory and change the 'EXPOSE 8080' line to a 
value of your choosing.

If using docker-compose, this can easily be changed by opening up the docker-compose file and changing both the
ports value as well as the Environemt Variable value "SERVER_PORT" to a value of your choosing. Here's a sample of
the file which will spin up the service on port 9006:
```
    ports:
      - "9006:9006"
    environment:
      - SERVER_PORT=9006
```
