package com.example.evaluationapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Survey{

    private String questions [] = {
            "Poster content is of professional quality and indicates a mastery of the project subject matter.",
            "The presentation is organized, engaging, and includes a thorough description of the design and the implementation of the design.",
            "All team members are suitably attired, are polite, demonstrate full knowledge of material, and can answer all relevant questions.",
            "The work product (model, prototype, documentation set or computer simulation) is of professional quality in all respects.",
            "The team implemented novel approaches and/or solutions in the development of the project.",
            "The project has the potential to enhance the reputation of the Innovative Computing Project and/or CCI/DSI",
            "The team successfully explained the scope and results of their project in no more than 5 minutes."
    };

     private Map<Integer, String> choices = new HashMap<Integer, String>() {{
         put(0, "Poor");
         put(1, "Fair");
         put(2, "Good");
         put(3, "Very Good");
         put(4, "Superior");
     }};


    public int getQuestionsListSize(){
        return questions.length;
    }

    public String getQuestion(int number){
        String question = questions[number];
        return question;
    }

    public int getScoreValue(String choice){
        int score_value = 0;
        for (Map.Entry<Integer, String> entry : choices.entrySet()){
            if (entry.getValue().equals(choice)) {
                score_value = entry.getKey();
                break;
            }
        }
        return score_value;
    }

    public String[] getQuestions() {
        return questions;
    }

    public void setQuestions(String[] questions) {
        this.questions = questions;
    }


    public Map<Integer, String> getChoices() {
        return choices;
    }

    public void setChoices(Map<Integer, String> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "questions=" + questions +
                ", choices=" + choices +
                '}';
    }
}
