package br.com.diegogusava.merapar.demo.controller.dto;

import java.time.LocalDateTime;

public class AnalysisDetailsDto {

    private LocalDateTime firstPost;
    private LocalDateTime lastPost;
    private long totalPost;
    private long totalAcceptedPost;
    private long avgScore;

    public LocalDateTime getFirstPost() {
        return firstPost;
    }

    public void setFirstPost(LocalDateTime firstPost) {
        this.firstPost = firstPost;
    }

    public LocalDateTime getLastPost() {
        return lastPost;
    }

    public void setLastPost(LocalDateTime lastPost) {
        this.lastPost = lastPost;
    }

    public long getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(long totalPost) {
        this.totalPost = totalPost;
    }

    public long getTotalAcceptedPost() {
        return totalAcceptedPost;
    }

    public void setTotalAcceptedPost(long totalAcceptedPost) {
        this.totalAcceptedPost = totalAcceptedPost;
    }

    public long getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(long avgScore) {
        this.avgScore = avgScore;
    }
}
