package com.christie.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyserResponse {

    private TextMetadata textMetadata;
    private List<String> errors = new ArrayList<>();
}
