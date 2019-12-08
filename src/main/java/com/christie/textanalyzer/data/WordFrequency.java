package com.christie.textanalyzer.data;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordFrequency {
    private Integer mostFrequent;
    private List<Integer> values;
}
