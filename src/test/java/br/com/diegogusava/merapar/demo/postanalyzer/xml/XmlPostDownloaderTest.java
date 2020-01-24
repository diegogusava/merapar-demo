package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.exception.FileUnavailableException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.XmlFileNotFoundException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import wiremock.org.eclipse.jetty.http.HttpHeader;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlPostDownloaderTest {

    private static final int PORT = 8089;
    private static WireMockServer wireMockServer;

    XmlPostDownloader downloader;
    String tempDirectory = "/tmp";

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(options().port(PORT));
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        this.downloader = new XmlPostDownloader(tempDirectory, new RestTemplate());
    }

    @Test
    void downloadFile() throws IOException {
        Path path = null;
        try {
            //given
            final String body = "Hello world!";
            final String fileUrl = "/file-download.xml";
            wireMockServer.stubFor(WireMock.get(fileUrl)
                    .withHeader(HttpHeader.ACCEPT.asString(), containing("xml"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", MediaType.TEXT_XML_VALUE)
                            .withBody(body)));

            //when
            path = this.downloader.downloadFile(getFileUrl(fileUrl));
            final String content = Files.readString(path);

            //then
            assertEquals(body, content);
        } finally {
            deleteFile(path);
        }
    }

    @Test
    void downloadFileNotFound() {
        Assertions.assertThrows(XmlFileNotFoundException.class, () -> {
            final String fileUrl = "/file-not-found.xml";
            wireMockServer.stubFor(WireMock.get(fileUrl)
                    .willReturn(aResponse().withStatus(404)));

            //when
            this.downloader.downloadFile(getFileUrl(fileUrl));
        });
    }

    @Test
    void downloadFileBadRequest() {
        Assertions.assertThrows(FileUnavailableException.class, () -> {
            final String fileUrl = "/file-not-found.xml";
            wireMockServer.stubFor(WireMock.get(fileUrl)
                    .willReturn(aResponse().withStatus(400)));

            //when
            this.downloader.downloadFile(getFileUrl(fileUrl));
        });
    }

    @Test
    void downloadFileServerUnavailable() {
        Assertions.assertThrows(FileUnavailableException.class, () -> {
            final String fileUrl = "/server-unavailable.xml";
            wireMockServer.stubFor(WireMock.get(fileUrl)
                    .willReturn(aResponse().withStatus(500)));

            //when
            this.downloader.downloadFile(getFileUrl(fileUrl));
        });
    }

    private URI getFileUrl(final String fileUrl) {
        return URI.create(format("http://localhost:%s%s", PORT, fileUrl));
    }

    private void deleteFile(final Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}