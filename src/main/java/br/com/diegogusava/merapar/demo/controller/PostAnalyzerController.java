package br.com.diegogusava.merapar.demo.controller;

import br.com.diegogusava.merapar.demo.controller.dto.AnalysisDetailsDto;
import br.com.diegogusava.merapar.demo.controller.dto.AnalysisDto;
import br.com.diegogusava.merapar.demo.postanalyzer.PostAnalyzerService;
import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class PostAnalyzerController {

    private PostAnalyzerService postAnalyzerService;

    public PostAnalyzerController(PostAnalyzerService postAnalyzerService) {
        this.postAnalyzerService = postAnalyzerService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AnalysisDto analyze(@RequestBody AnalysePostXmlRequest request) {
        final boolean isValidUrl = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS).isValid(request.getUrl());
        if (!isValidUrl) {
            throw new InvalidUrlException();
        }
        final Analysis analysis = postAnalyzerService.analyze(URI.create(request.getUrl()));
        return mapEntityToDto(analysis);
    }

    private AnalysisDto mapEntityToDto(Analysis analysis) {
        final var dto = new AnalysisDto();
        final var details = new AnalysisDetailsDto();
        dto.setDetails(details);
        dto.setAnalyseDate(analysis.getAnalyseDate());
        details.setFirstPost(analysis.getDetails().getFirstPost());
        details.setLastPost(analysis.getDetails().getLastPost());
        details.setTotalPost(analysis.getDetails().getTotalPost());
        details.setTotalAcceptedPost(analysis.getDetails().getTotalAcceptedPost());
        details.setAvgScore(analysis.getDetails().getAvgScore());
        return dto;
    }

}