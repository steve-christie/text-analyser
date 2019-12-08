package com.christie.textanalyzer.service;

import com.christie.textanalyzer.data.AnalyserResponse;

public interface Processor {

    AnalyserResponse parseData(String path);
}
