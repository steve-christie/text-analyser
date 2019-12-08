package com.christie.textanalyzer.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import com.christie.textanalyzer.data.AnalyserResponse;
import com.christie.textanalyzer.data.TextMetadata;
import com.christie.textanalyzer.data.WordFrequency;
import com.christie.textanalyzer.service.FileProcessorImpl;
import com.christie.textanalyzer.service.StringProcessorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TextAnalyserController.class)
@ContextConfiguration(classes = TextAnalyserController.class)
public class TestAnalyserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileProcessorImpl fileProcessor;

    @MockBean
    private StringProcessorImpl stringProcessor;

    @Test
    public void whenValidStringIsPassedInResultShouldBe200() throws Exception {

        AnalyserResponse mockResponse = AnalyserResponse.builder()
            .textMetadata(TextMetadata.builder()
                .averageWordLength(4.556)
                .wordCount(9)
                .wordLengths(Map.of(1, 1,
                    2, 1,
                    3, 1,
                    4, 2,
                    5, 2,
                    7, 1,
                    10, 1))
                .frequency(WordFrequency.builder()
                    .values(asList(4, 5))
                    .mostFrequent(2).build())
                .build())
            .build();

        when(stringProcessor.parseData(anyString())).thenReturn(mockResponse);

        mockMvc.perform(
            post("/string")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"Hello world & good morning. The date is 18/05/2016\"}"))
            .andExpect(status().isOk());
        //
        // .andExpect(content()..string("Word Count = 9\n" +
        //     "Average Word Length = 4.556\n" +
        //     "Number of words of length 1 is 1\n" +
        //     "Number of words of length 2 is 1\n" +
        //     "Number of words of length 3 is 1\n" +
        //     "Number of words of length 4 is 2\n" +
        //     "Number of words of length 5 is 2\n" +
        //     "Number of words of length 7 is 1\n" +
        //     "Number of words of length 10 is 1\n" +
        //     "The most frequently occurring word length is 2, for word lengths of 4 & 5\n"));
    }

    @Test
    public void whenInValidKeyIsPassedInThenResultShouldBe_400() throws Exception {

        mockMvc.perform(
            post("/string")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"texxt\":\"Hello world & good morning. The date is 18/05/2016\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenEmptyValueIsPassedInThenResultShouldBe_400() throws Exception {
        mockMvc.perform(
            post("/string")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenNullValueIsPassedInThenResultShouldBe_400() throws Exception {
        mockMvc.perform(
            post("/string")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidFileLocationIsProvidedThenResultShouldBe_200() throws Exception {
        AnalyserResponse mockResponse = AnalyserResponse.builder()
            .textMetadata(TextMetadata.builder()
                .averageWordLength(4.556)
                .wordCount(9)
                .wordLengths(Map.of(1, 1,
                    2, 1,
                    3, 1,
                    4, 2,
                    5, 2,
                    7, 1,
                    10, 1))
                .frequency(WordFrequency.builder()
                    .values(asList(4, 5))
                    .mostFrequent(2).build())
                .build())
            .errors(emptyList())
            .build();

        when(fileProcessor.parseData(anyString())).thenReturn(mockResponse);

        mockMvc.perform(
            post("/file")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"filePath\":\"/data/file.txt\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidFileLocationIsProvidedThenResultShouldBe_400() throws Exception {
        AnalyserResponse mockResponse = AnalyserResponse.builder()
            .errors(asList("Unable to load file, please check path"))
            .build();

        when(fileProcessor.parseData(anyString())).thenReturn(mockResponse);

        mockMvc.perform(
            post("/file")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"filePath\":\"/data/file.txt\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0]", is("Unable to load file, please check path")));
    }

}
