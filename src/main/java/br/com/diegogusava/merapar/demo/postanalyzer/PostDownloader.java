package br.com.diegogusava.merapar.demo.postanalyzer;

import java.net.URI;
import java.nio.file.Path;

/**
 * Responsible for downloading the file
 */
public interface PostDownloader {
    Path downloadFile(URI fileUri);
}
