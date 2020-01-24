package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.InvalidXmlContentException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.ParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class XmlPostParserTest {

    private XmlPostParser parser;

    @BeforeEach
    void setUp() {
        this.parser = new XmlPostParser();
    }

    @Test
    void parse() {
        final Path path = getFilePath("valid-content.xml");
        final Analysis result = parser.parse(path);
        assertNotNull(result.getAnalyseDate());
        final var details = result.getDetails();
        assertEquals(details.getFirstPost(), LocalDateTime.parse("2015-07-14T18:39:27.757"));
        assertEquals(details.getLastPost(), LocalDateTime.parse("2015-07-14T22:28:24.197"));
        assertEquals(details.getTotalPost(), 9);
        assertEquals(details.getTotalAcceptedPost(), 1);
        assertEquals(details.getAvgScore(), 2);
    }

    @Test
    void parseEmptyFile() {
        final Path path = getFilePath("empty-posts.xml");
        final Analysis result = parser.parse(path);
        assertEmptyResult(result);
    }

    @Test
    void parseInvalidXmlContent() {
        final Path path = getFilePath("invalid-content.xml");
        final Analysis result = parser.parse(path);
        assertEmptyResult(result);
    }

    private void assertEmptyResult(Analysis result) {
        assertNull(result.getDetails().getFirstPost());
        assertNull(result.getDetails().getLastPost());
        assertEquals(result.getDetails().getTotalPost(), 0);
        assertEquals(result.getDetails().getTotalAcceptedPost(), 0);
        assertEquals(result.getDetails().getAvgScore(), 0);
    }

    @Test
    void parseInvalidFile() {
        Assertions.assertThrows(ParserException.class, () -> {
            final Path path = getFilePath("invalid-file.txt");
            parser.parse(path);
        });
    }

    @Test
    void parseEmptyAcceptedAnswer() {
        Assertions.assertThrows(InvalidXmlContentException.class, () -> {
            final Path path = getFilePath("empty-accepted-answer.xml");
            parser.parse(path);
        });
    }

    @Test
    void parseInvalidCreationDate() {
        Assertions.assertThrows(InvalidXmlContentException.class, () -> {
            final Path path = getFilePath("invalid-creation-date.xml");
            parser.parse(path);
        });
    }

    private Path getFilePath(final String fileName) {
        try {
            return new ClassPathResource(fileName).getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}