package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentRegisterBinding;
import com.example.evaluationapp.databinding.FragmentTeamsBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamsFragment extends Fragment {

    FragmentTeamsBinding binding;
    ITeams am;

    User user;
    TeamsAdapter teamAdapter;
    ArrayList<Teams> teamArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Teams");

        binding = FragmentTeamsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = am.getUser();
        Log.d("demo", "onCreateView: " + user.getEmail());

        binding.name.setText(user.getFullname());

        binding.teamView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.teamView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.teamView.getContext(), llm.getOrientation());
        binding.teamView.addItemDecoration(dividerItemDecoration);

        am.getTeams(new MainActivity.Return() {
            @Override
            public void response(@NonNull String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Teams[] teams = gson.fromJson(response, Teams[].class);

                ArrayList<Teams> teamsArrayList = new ArrayList<>(Arrays.asList(teams));

                Log.d("demo", "response: " + teamsArrayList);

                binding.teamView.setAdapter(new TeamsAdapter(user, teamsArrayList));
            }

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public void error(@NonNull String response) {
            }
        });


        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ITeams) {
            am = (ITeams) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface ITeams {

        void alert(String msg);
        User getUser();
        void setUser(User user);
        void getTeams(MainActivity.Return response);
        void sendLoginView();
    }
}