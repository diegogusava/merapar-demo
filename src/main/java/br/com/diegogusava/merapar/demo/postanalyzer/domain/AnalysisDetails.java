package br.com.diegogusava.merapar.demo.postanalyzer.domain;

import java.time.LocalDateTime;

public class AnalysisDetails {

    private LocalDateTime firstPost;
    private LocalDateTime lastPost;
    private long totalPost;
    private long totalAcceptedPost;
    private long totalScore;

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

    public long getTotalAcceptedPost() {
        return totalAcceptedPost;
    }

    public long getAvgScore() {
        if (totalPost == 0) {
            return 0;
        }
        return totalScore / totalPost;
    }

    public void incrementPostCount() {
        this.totalPost++;
    }

    public void incrementAcceptedPostCount() {
        this.totalAcceptedPost++;
    }

    public void addScore(int score) {
        this.totalScore += score;
    }
}
