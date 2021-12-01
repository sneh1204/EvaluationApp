package com.example.evaluationapp;

public class Teams {

    String _id;
    String teamname;

    public Teams() {
    }

    public Teams(String _id, String teamname) {
        this._id = _id;
        this.teamname = teamname;
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

    @Override
    public String toString() {
        return "Teams{" +
                "_id='" + _id + '\'' +
                ", teamname='" + teamname + '\'' +
                '}';
    }
}
