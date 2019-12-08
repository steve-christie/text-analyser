package com.christie.textanalyzer.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMetadata {

    private long wordCount;
    private double averageWordLength;
    private Map<Integer, Integer> wordLengths = new HashMap<>();
    private WordFrequency frequency;


}
