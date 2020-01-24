package br.com.diegogusava.merapar.demo.controller;

import br.com.diegogusava.merapar.demo.postanalyzer.PostAnalyzerService;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.AnalysisDetails;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.FileUnavailableException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.InvalidXmlContentException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.ParserException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.XmlFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostAnalyzerController.class)
class PostAnalyzerControllerTest {

    private static final String FILE_URL = "http://merapar.com/file.xml";

    @MockBean
    PostAnalyzerService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void analyze() throws Exception {
        Analysis analysis = new Analysis();
        analysis.setDetails(new AnalysisDetails());
        when(service.analyze(any())).thenReturn(analysis);

        mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", FILE_URL))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is2xxSuccessful());

        verify(service).analyze(URI.create("http://merapar.com/file.xml"));
    }

    @Test
    void analyzeInvalidUrl() throws Exception {
        mockMvc.perform(
                post("/analyze")
                        .content("{\"url\":\"invalid-url\"}")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid URL"));
    }

    @Test
    void analyzeNoContent() throws Exception {
        mockMvc.perform(
                post("/analyze")
                        .content("{}")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is4xxClientError());
    }

    @Test
    void analyzeFileNotFound() throws Exception {
        when(service.analyze(any())).thenThrow(XmlFileNotFoundException.class);

        mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", FILE_URL))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("File not found"));
    }

    @Test
    void analyzeFileUnavailable() throws Exception {
        when(service.analyze(any())).thenThrow(FileUnavailableException.class);

        final ResultActions result = mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", FILE_URL))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("File is unavailable."));
    }

    @Test
    void analyzeParserError() throws Exception {
        when(service.analyze(any())).thenThrow(ParserException.class);

        final ResultActions result = mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", FILE_URL))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is5xxServerError());
    }

    @Test
    void analyzeInvalidContent() throws Exception {
        when(service.analyze(any())).thenThrow(new InvalidXmlContentException("Invalid content"));

        mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", FILE_URL))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid content"));
    }
}