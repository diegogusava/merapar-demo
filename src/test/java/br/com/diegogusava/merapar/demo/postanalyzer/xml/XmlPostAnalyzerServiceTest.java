package br.com.diegogusava.merapar.demo.postanalyzer.xml;

import br.com.diegogusava.merapar.demo.postanalyzer.PostDownloader;
import br.com.diegogusava.merapar.demo.postanalyzer.PostParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XmlPostAnalyzerServiceTest {

    private final static URI FILE_URI = URI.create("http://www.marepar.com");

    @Test
    void process(@Mock PostDownloader downloader, @Mock PostParser parser) {
        //given
        final XmlPostAnalyzerService service = new XmlPostAnalyzerService(downloader, parser);

        //when
        service.analyze(FILE_URI);

        //then
        verify(downloader).downloadFile(FILE_URI);
        verify(parser).parse(any());
    }

    @Test
    void deleteFileAfterProcessing(@Mock PostDownloader downloader, @Mock PostParser parser) throws IOException {
        //given
        final XmlPostAnalyzerService service = new XmlPostAnalyzerService(downloader, parser);
        Path path = Files.createFile(Path.of("/tmp", UUID.randomUUID().toString()));
        when(downloader.downloadFile(FILE_URI)).thenReturn(path);

        //when
        service.analyze(FILE_URI);

        //then
        assertThat(Files.exists(path)).isFalse();
    }
}