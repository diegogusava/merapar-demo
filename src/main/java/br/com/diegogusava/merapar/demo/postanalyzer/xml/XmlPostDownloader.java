package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.PostDownloader;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.FileUnavailableException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.XmlFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

import static java.lang.String.format;

@Component
class XmlPostDownloader implements PostDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPostDownloader.class);

    private String tempDirectory;
    private RestTemplate restTemplate;

    public XmlPostDownloader(@Value("${tempDirectory}") final String tempDirectory,
                             final RestTemplate restTemplate) {
        this.tempDirectory = tempDirectory;
        this.restTemplate = restTemplate;
    }

    public Path downloadFile(final URI fileUri) {
        try {
            return restTemplate.execute(fileUri, HttpMethod.GET, request -> {
                request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.TEXT_XML_VALUE);
                request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
            }, response -> {
                final Path ret = Path.of(tempDirectory, UUID.randomUUID().toString());
                StreamUtils.copy(response.getBody(), new FileOutputStream(ret.toFile()));
                return ret;
            });
        } catch (HttpClientErrorException.NotFound clientError) {
            throw new XmlFileNotFoundException();
        } catch (RestClientResponseException | ResourceAccessException serverError) {
            LOGGER.warn(format("File %s unavailable", fileUri), serverError);
            throw new FileUnavailableException();
        }
    }

}
