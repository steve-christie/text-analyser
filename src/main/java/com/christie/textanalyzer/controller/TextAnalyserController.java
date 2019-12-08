package com.christie.textanalyzer.controller;

import java.util.Map;
import java.util.stream.Collectors;

import com.christie.textanalyzer.controller.commands.AnalyseFileCommand;
import com.christie.textanalyzer.controller.commands.AnalyseStringCommand;
import com.christie.textanalyzer.data.AnalyserResponse;
import com.christie.textanalyzer.data.TextMetadata;
import com.christie.textanalyzer.service.FileProcessorImpl;
import com.christie.textanalyzer.service.StringProcessorImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TextAnalyserController {

    private final StringProcessorImpl stringProcessor;
    private final FileProcessorImpl fileProcessor;

    @PostMapping("/string")
    public ResponseEntity analyseString(@RequestBody AnalyseStringCommand command) {


        if (StringUtils.isEmpty(command.getText())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid Request Body, text field must be populated"));
        }
        return ResponseEntity
            .ok(convertTextDetailIntoStringResponse(stringProcessor.parseData(command.getText()).getTextMetadata()));
    }

    @PostMapping("/file")
    public ResponseEntity analyseFile(@RequestBody AnalyseFileCommand command) {

        AnalyserResponse response = fileProcessor.parseData(command.getFilePath());

        if (response.getErrors().isEmpty()) {

            return ResponseEntity.ok(convertTextDetailIntoStringResponse(
                response.getTextMetadata()));
        }

        return ResponseEntity.badRequest().body(Map.of("errors", response.getErrors()));
    }

    private String convertTextDetailIntoStringResponse(TextMetadata detail) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Word Count = %s%n", detail.getWordCount()));
        builder.append(String.format("Average Word Length = %s%n", detail.getAverageWordLength()));
        detail.getWordLengths()
            .forEach((k, v) -> builder.append(String.format("Number of words of length %s is %s%n", k, v)));
        builder.append(String.format("The most frequently occurring word length is %s, for word lengths of %s%n",
            detail.getFrequency().getMostFrequent(),
            detail.getFrequency().getValues().stream().map(Object::toString).collect(Collectors.joining(" & "))));
        return builder.toString();
    }
}
