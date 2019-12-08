package com.christie.textanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.christie.textanalyzer.data.TextMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StringProcessorImpl.class)
public class StringProcessorImplTest {

    @Autowired
    private StringProcessorImpl processor;

    @Test
    public void parseStringTest() {
        TextMetadata detail =
            processor.parseData("Hello world & good morning. The date is 18/05/2016").getTextMetadata();
        assertThat(detail.getWordCount()).isEqualTo(9);
        assertThat(detail.getAverageWordLength()).isEqualTo(4.556);
        assertThat(detail.getWordLengths().get(1)).isEqualTo(1);
        assertThat(detail.getWordLengths().get(2)).isEqualTo(1);
        assertThat(detail.getWordLengths().get(3)).isEqualTo(1);
        assertThat(detail.getWordLengths().get(5)).isEqualTo(2);
        assertThat(detail.getWordLengths().get(5)).isEqualTo(2);
        assertThat(detail.getWordLengths().get(7)).isEqualTo(1);
        assertThat(detail.getWordLengths().get(10)).isEqualTo(1);
    }

}
