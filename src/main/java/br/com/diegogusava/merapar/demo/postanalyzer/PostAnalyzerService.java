package br.com.diegogusava.merapar.demo.postanalyzer;

import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;

import java.net.URI;

/**
 * Responsible for analyzing the file content
 */
public interface PostAnalyzerService {
    Analysis analyze(URI uri);
}
