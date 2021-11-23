package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentResultBinding;
import com.example.evaluationapp.model.User;


public class ResultFragment extends Fragment {

    FragmentResultBinding binding;
    static final String ARG_PARAM_TOTALSCORE = "totalScore";
    IResult am;

    int totalScore;

    public static ResultFragment newInstance(int total_score) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_TOTALSCORE, total_score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalScore = getArguments().getInt(ARG_PARAM_TOTALSCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.result);

        binding = FragmentResultBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.score.setText(String.valueOf(totalScore));

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.setUser(null);
                am.sendLoginView();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IResult) {
            am = (IResult) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IResult {
        void setUser(User user);
        void sendLoginView();
    }

}