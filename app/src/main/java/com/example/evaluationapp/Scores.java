package com.example.evaluationapp;

import java.io.Serializable;

public class Scores implements Serializable {
    String examinername;
    int score;

    public Scores() {
    }

    public Scores(String examinername, int score) {
        this.examinername = examinername;
        this.score = score;
    }

    public String getExaminername() {
        return examinername;
    }

    public void setExaminername(String examinername) {
        this.examinername = examinername;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Scores{" +
                "examinername='" + examinername + '\'' +
                ", score=" + score +
                '}';
    }
}
