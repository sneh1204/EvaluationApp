package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.evaluationapp.databinding.FragmentSurveyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SurveyFragment extends Fragment {

    FragmentSurveyBinding binding;
    ISurvey am;
    Survey surveyObject = new Survey();
    User user;

    int questionNumber = 0;
    int total_score = 0;
    String choice;
    static int listSize;
    double teamAverageScore = 0;

    static final String ARG_PARAM_TEAM = "team";
    Teams team;

    public static SurveyFragment newInstance(Teams team) {
        SurveyFragment fragment = new SurveyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team = (Teams) getArguments().getSerializable(ARG_PARAM_TEAM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.survey);

        binding = FragmentSurveyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        user = am.getUser();
        listSize = surveyObject.getQuestionsListSize();

        updateQuestion();

        // On Next Button
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = binding.getRoot().findViewById(binding.choices.getCheckedRadioButtonId());
                choice = radioButton.getText().toString();
                updateScore(choice);
                updateQuestion();
            }
        });


        // On Finish Button
        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = binding.getRoot().findViewById(binding.choices.getCheckedRadioButtonId());
                choice = radioButton.getText().toString();
                updateScore(choice);
                calculateAverageScore(total_score);

                am.update(new com.example.evaluationapp.MainActivity.Return() {
                    @Override
                    public void response(@NotNull String response) {
                        Log.d("demo", "response: " + response);
                        am.sendResultView();
                    }

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public void error(@NotNull String response) {
                    }

                }, user.getFullname(), String.valueOf(total_score), team.getTeamname(),  String.format("%.2f", teamAverageScore));

            }
        });

        return view;

    }

    public void updateQuestion(){
        setButtonVisibility();
        binding.question.setText(surveyObject.getQuestion(questionNumber));
        questionNumber++;

    }

    public void updateScore(String choice){
        int score = surveyObject.getScoreValue(choice);
        total_score = total_score + score;
        Log.d("demo", "total Score : " + total_score);
    }

    public void calculateAverageScore(int total_score){
        ArrayList<Scores> scoresArrayList = team.getScores();
        int numOfEvaluations;
        if(scoresArrayList == null){
            numOfEvaluations = 1;
        }else{
            numOfEvaluations = scoresArrayList.size() + 1;
        }

        double averageScore = team.getAvgscore();
        teamAverageScore = (averageScore + total_score)/ numOfEvaluations;
    }

    public void setButtonVisibility(){
        if(questionNumber != listSize - 1){
            binding.next.setEnabled(true);
            binding.finish.setVisibility(View.GONE);
        }else{
            binding.next.setEnabled(false);
            binding.finish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ISurvey) {
            am = (ISurvey) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface ISurvey {
        User getUser();
        void update(com.example.evaluationapp.MainActivity.Return response, String ...data);
        void sendResultView();
    }
}