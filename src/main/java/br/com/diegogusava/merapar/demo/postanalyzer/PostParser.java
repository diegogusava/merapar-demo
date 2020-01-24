package br.com.diegogusava.merapar.demo.postanalyzer;

import br.com.diegogusava.merapar.demo.postanalyzer.domain.Analysis;

import java.nio.file.Path;

/**
 * Responsible for parser the file
 */
public interface PostParser {
    Analysis parse(Path path);
}

