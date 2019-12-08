# Text Analyser
This service is designed to take input in the form of a string or the path to a file. The output will inform the 
user on some meta data about the input, notably the number of words and the average word length. The service is
accessed via a REST API.

## Api
The API is available at the following paths:
* POST: `/text_analyser/string`
* POST: `/text_analyser/file`

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
