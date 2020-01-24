package br.com.diegogusava.merapar.demo.controller.dto;

import java.time.LocalDateTime;

public class AnalysisDto {
    private LocalDateTime analyseDate;
    private AnalysisDetailsDto details;

    public LocalDateTime getAnalyseDate() {
        return analyseDate;
    }

    public void setAnalyseDate(LocalDateTime analyseDate) {
        this.analyseDate = analyseDate;
    }

    public AnalysisDetailsDto getDetails() {
        return details;
    }

    public void setDetails(AnalysisDetailsDto details) {
        this.details = details;
    }
}
