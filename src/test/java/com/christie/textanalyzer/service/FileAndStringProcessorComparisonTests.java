package com.christie.textanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.christie.textanalyzer.data.TextMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest(classes = {StringProcessorImpl.class, FileProcessorImpl.class})
public class FileAndStringProcessorComparisonTests {

    @Autowired
    private FileProcessorImpl fileProcessor;

    @Autowired
    private StringProcessorImpl stringProcessor;

    @Test
    public void whenSameTextIsComparedByBothProcessorsThenAverageShouldBeTheSame() throws Exception {


        String sample = "Hello world & good morning. The date is 18/05/2016";
        String sampleFilePath = new ClassPathResource("sample_split_lines_file.txt").getURI().getPath();

        TextMetadata stringMeta = stringProcessor.parseData(sample).getTextMetadata();
        TextMetadata fileMeta = fileProcessor.parseData(sampleFilePath).getTextMetadata();

        assertThat(stringMeta.getAverageWordLength()).isEqualTo(fileMeta.getAverageWordLength());
    }
}
