package com.christie.textanalyzer.service;

import static com.christie.textanalyzer.service.ProcessorUtils.calculateWordFrequency;
import static com.christie.textanalyzer.service.ProcessorUtils.getAverageLengthOfStringsInArray;
import static com.christie.textanalyzer.service.ProcessorUtils.removeSpecialCharactersFromString;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import com.christie.textanalyzer.data.WordFrequency;
import org.junit.jupiter.api.Test;

public class ProcessorUtilslTests {

    @Test
    public void whenSingularWordOfLengthFourThenAverageLengthShouldBeFour() {
        OptionalDouble result = getAverageLengthOfStringsInArray(Stream.of("test"));
        assertThat(result.getAsDouble()).isEqualTo(4);
    }

    @Test
    public void whenTwoWordsOfLengthFourThenAverageLengthShouldBeFour() {
        OptionalDouble result = getAverageLengthOfStringsInArray(Stream.of("test", "test"));
        assertThat(result.getAsDouble()).isEqualTo(4);
    }

    @Test
    public void whenTwoWordsOfLengthsFourAndSixThenAverageLengthShouldBeFive() {
        OptionalDouble result = getAverageLengthOfStringsInArray(Stream.of("test", "tester"));
        assertThat(result.getAsDouble()).isEqualTo(5);
    }

    @Test
    public void whenStringWithSpecialCharsProvidedThenAllShouldBeRemoved() {
        String sample = "te?:;()*.,st";
        String result = removeSpecialCharactersFromString(sample);
        assertThat(result).isEqualTo("test");
    }

    @Test
    public void whenSingleMostOccurringWordLengthIsFoundThenOnlyThisShouldExistInResult() {
        Map<Integer, Integer> lengths = Map.of(1, 2,
            2, 1,
            3, 1,
            4, 2,
            5, 3);

        WordFrequency frequency = calculateWordFrequency(lengths);
        assertThat(frequency.getMostFrequent()).isEqualTo(3);
        assertThat(frequency.getValues()).hasSize(1);
        assertThat(frequency.getValues()).contains(5);
    }

    @Test
    public void whenMultipleOccurringWordLengthAreFoundThenOnlyTheseShouldExistInResult() {
        Map<Integer, Integer> lengths = Map.of(1, 2,
            2, 2,
            3, 1);

        WordFrequency frequency = calculateWordFrequency(lengths);
        assertThat(frequency.getMostFrequent()).isEqualTo(2);
        assertThat(frequency.getValues()).hasSize(2);
        assertThat(frequency.getValues()).contains(1, 2);
    }
}
