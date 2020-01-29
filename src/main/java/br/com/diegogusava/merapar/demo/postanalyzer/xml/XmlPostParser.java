package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.PostParser;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.AnalysisDetails;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.InvalidXmlContentException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static java.lang.String.format;

@Component
class XmlPostParser implements PostParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPostParser.class);

    @Override
    public Analysis parse(final Path xmlPath) {
        var result = new Analysis();
        var details = new AnalysisDetails();
        result.setDetails(details);
        readContent(details, xmlPath);
        return result;
    }

    private void readContent(final AnalysisDetails details, final Path xmlPath) {
        try (var inputStream = new FileInputStream(xmlPath.toFile())){
            final var xmlInputFactory = XMLInputFactory.newInstance();
            final var reader = xmlInputFactory.createXMLEventReader(inputStream);
            while (reader.hasNext()) {
                processEvent(details, reader.nextEvent());
            }
        } catch (XMLStreamException e) {
            final String errorMsg = format("Invalid file %s", xmlPath);
            LOGGER.error(errorMsg, e);
            throw new ParserException();
        } catch (FileNotFoundException e) {
            LOGGER.error(format("Xml file %s not found", xmlPath), e);
            throw new ParserException();
        } catch (IOException e) {
            LOGGER.error(format("Error reading XML file %s", xmlPath), e);
            throw new ParserException();
        }
    }

    private void processEvent(final AnalysisDetails details, final XMLEvent nextEvent) {
        if (nextEvent.isStartElement()) {
            final var startElement = nextEvent.asStartElement();
            if ("row".equalsIgnoreCase(startElement.getName().getLocalPart())) {
                processRow(details, startElement);
            }
        }
    }

    private void processRow(final AnalysisDetails details, final StartElement startElement) {
        final LocalDateTime creationDate = getCreationDate(startElement);
        if (details.getFirstPost() == null) {
            details.setFirstPost(creationDate);
        }
        details.setLastPost(creationDate);
        details.incrementPostCount();

        if (isAnswerAccepted(startElement)) {
            details.incrementAcceptedPostCount();
        }

        final Attribute scoreAttr = startElement.getAttributeByName(QName.valueOf("Score"));
        if (scoreAttr != null) {
            details.addScore(Integer.parseInt(scoreAttr.getValue()));
        }
    }

    private boolean isAnswerAccepted(StartElement startElement) {
        final Attribute acceptedAnswerId = startElement.getAttributeByName(QName.valueOf("AcceptedAnswerId"));
        if (acceptedAnswerId == null) {
            return false;
        }

        boolean isValidNumber = acceptedAnswerId.getValue().matches("\\d+");
        if (!isValidNumber) {
            throw new InvalidXmlContentException("Attribute AcceptedAnswerId is invalid");
        }
        return true;
    }

    private LocalDateTime getCreationDate(StartElement startElement) {
        final Attribute creationDateAttr = startElement.getAttributeByName(QName.valueOf("CreationDate"));
        if (creationDateAttr == null) {
            throw new InvalidXmlContentException("Attribute CreationDate not found");
        }
        try {
            return LocalDateTime.parse(creationDateAttr.getValue());
        } catch (Exception e) {
            throw new InvalidXmlContentException("Attribute CreationDate is invalid");
        }
    }

}