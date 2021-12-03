package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentExaminerBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;


public class ExaminerFragment extends Fragment {

    FragmentExaminerBinding binding;
    IExaminer am;

    ExaminerAdapter examinerAdapter;
    ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Examiners");

        binding = FragmentExaminerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.examinerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.examinerView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.examinerView.getContext(), llm.getOrientation());
        binding.examinerView.addItemDecoration(dividerItemDecoration);

        am.getExaminers(new MainActivity.Return() {
            @Override
            public void response(@NonNull String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                User[] users = gson.fromJson(response, User[].class);

                ArrayList<User> userArrayList = new ArrayList<>(Arrays.asList(users));

                binding.examinerView.setAdapter(new ExaminerAdapter(userArrayList));
            }

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public void error(@NonNull String response) {
            }
        });

        binding.createExaminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendCreateExaminerView();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendAdminPortalView();
            }
        });


        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IExaminer) {
            am = (IExaminer) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IExaminer {
        void getExaminers(MainActivity.Return response);
        void goBack();
        void sendAdminPortalView();
        void sendCreateExaminerView();
    }
}