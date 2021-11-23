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
import com.example.evaluationapp.model.Survey;

import java.util.HashMap;
import java.util.Map;

public class SurveyFragment extends Fragment {

    FragmentSurveyBinding binding;
    ISurvey am;
    Survey surveyObject = new Survey();

    int questionNumber = 0;
    int total_score = 0;
    String choice;
    static int listSize;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.survey);

        binding = FragmentSurveyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
                am.sendResultView(total_score);
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
        void sendResultView(int total_score);
    }
}