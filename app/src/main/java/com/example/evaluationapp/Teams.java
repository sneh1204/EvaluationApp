package com.example.evaluationapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Teams implements Serializable {

    String _id;
    String teamname;
    String city;
    int participants;
    ArrayList<Scores> scores;
    double avgscore;

    public Teams() {
    }

    public Teams(String _id, String teamname, String city, int participants, ArrayList<Scores> scores, double avgscore) {
        this._id = _id;
        this.teamname = teamname;
        this.city = city;
        this.participants = participants;
        this.scores = scores;
        this.avgscore = avgscore;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public ArrayList<Scores> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Scores> scores) {
        this.scores = scores;
    }

    public double getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(double avgscore) {
        this.avgscore = avgscore;
    }


    @Override
    public String toString() {
        return "Teams{" +
                "_id='" + _id + '\'' +
                ", teamname='" + teamname + '\'' +
                ", city='" + city + '\'' +
                ", participants=" + participants +
                ", scores=" + scores +
                ", avgscore=" + avgscore +
                '}';
    }
}
