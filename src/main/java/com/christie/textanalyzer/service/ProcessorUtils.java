package com.christie.textanalyzer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import com.christie.textanalyzer.data.TextMetadata;
import com.christie.textanalyzer.data.WordFrequency;
import org.apache.commons.math3.util.Precision;

public class ProcessorUtils {

    public TextMetadata parseString(String text) {

        String[] words = Stream.of(removeSpecialCharactersFromString(text).split(" "))
            .filter(word -> !word.equals("")).toArray(String[]::new);

        double averageLength = getAverageLengthOfStringsInArray(Stream.of(words)).orElse(0.0);
        Map<Integer, Integer> lengths = countLengthOfEachWordAndCollect(Stream.of(words));

        WordFrequency frequency = calculateWordFrequency(lengths);

        return TextMetadata.builder()
            .wordCount(words.length)
            .averageWordLength(Precision.round(averageLength, 3))
            .wordLengths(lengths)
            .frequency(frequency)
            .words(Arrays.asList(words))
            .build();
    }

    public OptionalDouble getAverageLengthOfStringsInArray(Stream<String> words) {
        return words.map(String::length)
            .mapToDouble(Integer::doubleValue)
            .average();
    }

    public Map<Integer, Integer> countLengthOfEachWordAndCollect(Stream<String> words) {
        Map<Integer, Integer> lengths = new HashMap<>();
        words.forEach(word -> incrementCountsInMap(lengths, word.length(), 1));
        return lengths;
    }

    public void incrementCountsInMap(Map<Integer, Integer> lengths, Integer length, Integer incrementalValue) {
        if (lengths.containsKey(length)) {
            lengths.put(length, lengths.get(length) + incrementalValue);
        } else {
            lengths.put(length, incrementalValue);
        }
    }

    public String removeSpecialCharactersFromString(String word) {
        return word.replace("?", "")
            .replace(".(", " ")
            .replace(":", "")
            .replace(";", "")
            .replace(")", "")
            .replace("(", "")
            .replace("*", "")
            .replace(".", "")
            .replace(",", "");
    }

    public WordFrequency calculateWordFrequency(Map<Integer, Integer> lengths) {
        Integer highestValue = lengths.values().stream().max(Integer::compareTo).get();

        List<Integer> l = new ArrayList<>();
        lengths.keySet().forEach(key -> {
            if (lengths.get(key) == highestValue) {
                l.add(key);
            }
        });

        return WordFrequency.builder()
            .mostFrequent(highestValue)
            .values(l).build();
    }
}
