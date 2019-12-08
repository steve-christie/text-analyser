package com.christie.textanalyzer.service;

import static java.util.Collections.singletonList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.christie.textanalyzer.data.AnalyserResponse;
import com.christie.textanalyzer.data.TextMetadata;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorImpl extends ProcessorUtils implements Processor {

    public AnalyserResponse parseData(String path) {
        AnalyserResponse response = new AnalyserResponse();
        List<TextMetadata> lineMetadata = new ArrayList<>();
        File file = new File(path);
        List<String> wordsLengths = new ArrayList<>();
        createLineIteratorFromFile(file).ifPresentOrElse(iterator ->
                iterateOverLines(iterator, lineMetadata, wordsLengths),
            () -> response.setErrors(singletonList("Unable to load file, please check path")));

        TextMetadata metadata = collateMetaData(lineMetadata, wordsLengths);
        response.setTextMetadata(metadata);
        return response;
    }

    private Optional<LineIterator> createLineIteratorFromFile(File file) {
        try {
            return Optional.of(FileUtils.lineIterator(file, StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void iterateOverLines(LineIterator iterator, List<TextMetadata> lineMetadata, List<String> words) {
        try {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (!line.equals("")) {
                    TextMetadata meta = parseString(line);
                    lineMetadata.add(meta);
                    words.addAll(meta.getWords());
                }
            }
        } finally {
            LineIterator.closeQuietly(iterator);
        }
    }

    private TextMetadata collateMetaData(List<TextMetadata> lineMetadata, List<String> words) {
        TextMetadata detail = new TextMetadata();

        if (lineMetadata.isEmpty()) {
            return detail;
        }


        lineMetadata.forEach(line -> {
            long newWordCount = line.getWordCount() + detail.getWordCount();
            detail.setWordCount(newWordCount);
            line.getWordLengths().forEach((k, v) -> incrementCountsInMap(detail.getWordLengths(), k, v));
            detail.setAverageWordLength(line.getAverageWordLength() + detail.getAverageWordLength());
        });
        detail.setAverageWordLength(Precision.round(getAverageLengthOfStringsInArray(words.stream()).getAsDouble(), 3));
        detail.setFrequency(calculateWordFrequency(detail.getWordLengths()));
        return detail;
    }
}
