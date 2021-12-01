package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentResultBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ResultFragment extends Fragment {

    FragmentResultBinding binding;
    IResult am;

    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.result);

        binding = FragmentResultBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = am.getUser();

        binding.resultView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.resultView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.resultView.getContext(), llm.getOrientation());
        binding.resultView.addItemDecoration(dividerItemDecoration);

        am.getTeams(new MainActivity.Return() {
            @Override
            public void response(@NonNull String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Teams[] teams = gson.fromJson(response, Teams[].class);

                ArrayList<Teams> teamsArrayList = new ArrayList<>(Arrays.asList(teams));

                binding.resultView.setAdapter(new ResultAdapter(user, teamsArrayList));
            }

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public void error(@NonNull String response) {
            }
        });


        binding.goToTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendTeamView();
            }
        });

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
        User getUser();
        void setUser(User user);
        void sendLoginView();
        void sendTeamView();
        void getTeams(MainActivity.Return response);
    }

}