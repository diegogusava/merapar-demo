package br.com.diegogusava.merapar.demo.postanalyzer.domain;

import java.time.LocalDateTime;

public class Analysis {

    private LocalDateTime analyseDate = LocalDateTime.now();
    private AnalysisDetails details;

    public LocalDateTime getAnalyseDate() {
        return analyseDate;
    }

    public AnalysisDetails getDetails() {
        return details;
    }

    public void setDetails(AnalysisDetails details) {
        this.details = details;
    }
}
