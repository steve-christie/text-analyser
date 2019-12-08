package com.christie.textanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import com.christie.textanalyzer.data.WordFrequency;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProcessorUtilslTests {

    ProcessorUtils processorUtils = new ProcessorUtils();

    @Test
    public void whenSingularWordOfLengthFourThenAverageLengthShouldBeFour() {
        OptionalDouble result = processorUtils.getAverageLengthOfStringsInArray(Stream.of("test"));
        assertThat(result.getAsDouble()).isEqualTo(4);
    }

    @Test
    public void whenTwoWordsOfLengthFourThenAverageLengthShouldBeFour() {
        OptionalDouble result = processorUtils.getAverageLengthOfStringsInArray(Stream.of("test", "test"));
        assertThat(result.getAsDouble()).isEqualTo(4);
    }

    @Test
    public void whenTwoWordsOfLengthsFourAndSixThenAverageLengthShouldBeFive() {
        OptionalDouble result = processorUtils.getAverageLengthOfStringsInArray(Stream.of("test", "tester"));
        assertThat(result.getAsDouble()).isEqualTo(5);
    }

    @Test
    public void whenStringWithSpecialCharsProvidedThenAllShouldBeRemoved() {
        String sample = "te?:;()*.,st";
        String result = processorUtils.removeSpecialCharactersFromString(sample);
        assertThat(result).isEqualTo("test");
    }

    @Test
    public void whenSingleMostOccurringWordLengthIsFoundThenOnlyThisShouldExistInResult() {
        Map<Integer, Integer> lengths = Map.of(1, 2,
            2, 1,
            3, 1,
            4, 2,
            5, 3);

        WordFrequency frequency = processorUtils.calculateWordFrequency(lengths);
        assertThat(frequency.getMostFrequent()).isEqualTo(3);
        assertThat(frequency.getValues()).hasSize(1);
        assertThat(frequency.getValues()).contains(5);
    }

    @Test
    public void whenMultipleOccurringWordLengthAreFoundThenOnlyTheseShouldExistInResult() {
        Map<Integer, Integer> lengths = Map.of(1, 2,
            2, 2,
            3, 1);

        WordFrequency frequency = processorUtils.calculateWordFrequency(lengths);
        assertThat(frequency.getMostFrequent()).isEqualTo(2);
        assertThat(frequency.getValues()).hasSize(2);
        assertThat(frequency.getValues()).contains(1, 2);
    }

    @Test
    public void whenNewLengthIsIncrementedThenValueIsIncreasedByProvidedAmount() {
        Map<Integer, Integer> sample = new HashMap<>();
        sample.put(1, 1);
        processorUtils.incrementCountsInMap(sample, 2, 2);
        assertThat(sample.get(1)).isEqualTo(1);
        assertThat(sample.get(2)).isEqualTo(2);
    }

    @Test
    public void whenExistingLengthIsIncrementedThenValueIsIncreasedByProvidedAmount() {
        Map<Integer, Integer> sample = new HashMap<>();
        sample.put(1, 1);
        processorUtils.incrementCountsInMap(sample, 1, 2);
        assertThat(sample.get(1)).isEqualTo(3);
    }

    @Test
    public void whenWordsArePassedInThenResultingMapShouldBePopulated() {
        Map<Integer, Integer> result =
            processorUtils.countLengthOfEachWordAndCollect(Stream.of("foo", "bar", "baz", "foobar"));
        assertThat(result.get(3)).isEqualTo(3);
        assertThat(result.get(6)).isEqualTo(1);
    }
}
