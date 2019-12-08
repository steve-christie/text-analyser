package com.christie.textanalyzer.service;

import static com.christie.textanalyzer.service.ProcessorUtils.calculateWordFrequency;
import static com.christie.textanalyzer.service.ProcessorUtils.incrementCountsInMap;
import static com.christie.textanalyzer.service.ProcessorUtils.parseString;
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
public class FileProcessorImpl implements Processor {

    public AnalyserResponse parseData(String path) {
        AnalyserResponse response = new AnalyserResponse();
        List<TextMetadata> lineDetails = new ArrayList<>();
        File file = new File(path);
        createLineIteratorFromFile(file).ifPresentOrElse(iterator ->
                iterateOverLines(iterator, lineDetails),
            () -> response.setErrors(singletonList("Unable to load file, please check path")));

        TextMetadata metadata = collectTextDetail(lineDetails);
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

    private void iterateOverLines(LineIterator iterator, List<TextMetadata> lineDetails) {
        try {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (!line.equals("")) {
                    lineDetails.add(parseString(line));
                }
            }
        } finally {
            LineIterator.closeQuietly(iterator);
        }
    }

    private TextMetadata collectTextDetail(List<TextMetadata> lineDetails) {
        TextMetadata detail = new TextMetadata();

        if (lineDetails.isEmpty()) {
            return detail;
        }

        lineDetails.forEach(line -> {
            long newWordCount = line.getWordCount() + detail.getWordCount();
            detail.setWordCount(newWordCount);
            line.getWordLengths().forEach((k, v) -> incrementCountsInMap(detail.getWordLengths(), k, v));
            detail.setAverageWordLength(line.getAverageWordLength() + detail.getAverageWordLength());
        });
        detail.setAverageWordLength(Precision.round(detail.getAverageWordLength() / lineDetails.size(), 3));
        detail.setFrequency(calculateWordFrequency(detail.getWordLengths()));
        return detail;
    }
}
