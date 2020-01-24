package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.PostAnalyzerService;
import br.com.diegogusava.merapar.demo.postanalyzer.PostDownloader;
import br.com.diegogusava.merapar.demo.postanalyzer.PostParser;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

@Service
class XmlPostAnalyzerService implements PostAnalyzerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPostAnalyzerService.class);

    private PostDownloader postDownloader;
    private PostParser postParser;

    public XmlPostAnalyzerService(PostDownloader postDownloader, PostParser postParser) {
        this.postDownloader = postDownloader;
        this.postParser = postParser;
    }

    @Override
    public Analysis analyze(final URI xmlUri) {
        Path xmlPath = null;
        try {
            xmlPath = postDownloader.downloadFile(xmlUri);
            return postParser.parse(xmlPath);
        } finally {
            deleteFile(xmlPath);
        }
    }

    private void deleteFile(final Path xmlPath) {
        if (xmlPath == null) {
            return;
        }
        try {
            Files.delete(xmlPath);
        } catch (IOException e) {
            LOGGER.warn(format("Error deleting file %s", xmlPath), e);
        }
    }
}
