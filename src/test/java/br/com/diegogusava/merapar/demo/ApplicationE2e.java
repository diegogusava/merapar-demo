package br.com.diegogusava.merapar.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wiremock.org.eclipse.jetty.http.HttpHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ApplicationE2e {

    private static final int MOCK_PORT = 8089;
    private static final String FILE_URL = "/valid-content.xml";
    private static WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(options().port(MOCK_PORT));
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void processFile() throws Exception {
        setUpMockServer();
        final var url = format("http://localhost:%s%s", MOCK_PORT, FILE_URL);
        mockMvc.perform(
                post("/analyze")
                        .content(format("{\"url\":\"%s\"}", url))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        ).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.analyseDate").exists())
                .andExpect(jsonPath("$.details.firstPost").value("2015-07-14T18:39:27.757"))
                .andExpect(jsonPath("$.details.lastPost").value("2015-07-14T22:28:24.197"))
                .andExpect(jsonPath("$.details.totalPost").value("9"))
                .andExpect(jsonPath("$.details.totalAcceptedPost").value("1"))
                .andExpect(jsonPath("$.details.avgScore").value("2"));
    }


    void setUpMockServer() throws IOException {
        String content = Files.readString(getFilePath("valid-content.xml"));
        wireMockServer.stubFor(WireMock.get(FILE_URL)
                .withHeader(HttpHeader.ACCEPT.asString(), containing("xml"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.TEXT_XML_VALUE)
                        .withBody(content)));
    }

    private Path getFilePath(final String fileName) {
        try {
            return new ClassPathResource(fileName).getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
