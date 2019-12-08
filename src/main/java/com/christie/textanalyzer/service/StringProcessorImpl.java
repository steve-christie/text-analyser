package com.christie.textanalyzer.service;

import static com.christie.textanalyzer.service.ProcessorUtils.parseString;

import com.christie.textanalyzer.data.AnalyserResponse;
import org.springframework.stereotype.Service;

@Service
public class StringProcessorImpl implements Processor {

    @Override
    public AnalyserResponse parseData(String text) {
        AnalyserResponse response = new AnalyserResponse();
        response.setTextMetadata(parseString(text));
        return response;
    }
}
