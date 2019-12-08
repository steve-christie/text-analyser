package com.christie.textanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.christie.textanalyzer.data.AnalyserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest(classes = FileProcessorImpl.class)
public class FileProcessorImplTests {

    @Autowired
    private FileProcessorImpl processor;

    @Test
    public void whenLocatableFileExistsThenMetaDataResultShouldBeReturned() throws Exception {

        String sampleFilePath = new ClassPathResource("sample_file.txt").getURI().getPath();
        AnalyserResponse response = processor.parseData(sampleFilePath);
        assertThat(response.getTextMetadata().getWordCount()).isEqualTo(7);
        assertThat(response.getErrors()).hasSize(0);
    }

    @Test
    public void whenLocatableFileDoesNotExistThenErrorsShouldBePopulated() throws Exception {

        AnalyserResponse response = processor.parseData("unknown.txt");
        assertThat(response.getErrors()).hasSize(1);
    }
}
