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

import com.example.evaluationapp.databinding.AdminteamViewBinding;
import com.example.evaluationapp.databinding.FragmentTeamViewBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamViewFragment extends Fragment {

    FragmentTeamViewBinding binding;
    ITeamView am;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Teams");

        binding = FragmentTeamViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.adminteamView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.adminteamView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.adminteamView.getContext(), llm.getOrientation());
        binding.adminteamView.addItemDecoration(dividerItemDecoration);

        am.getTeams(new MainActivity.Return() {
            @Override
            public void response(@NonNull String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Teams[] teams = gson.fromJson(response, Teams[].class);

                ArrayList<Teams> teamsArrayList = new ArrayList<>(Arrays.asList(teams));
                Log.d("demo", "Teams : " + teamsArrayList);
                binding.adminteamView.setAdapter(new TeamViewAdapter(teamsArrayList));

            }

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public void error(@NonNull String response) {
            }
        });


        binding.createTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendCreateTeamView();

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
        if (context instanceof ITeamView) {
            am = (ITeamView) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface ITeamView {
        void getTeams(MainActivity.Return response);
        void sendAdminPortalView();
        void sendCreateTeamView();
    }

}