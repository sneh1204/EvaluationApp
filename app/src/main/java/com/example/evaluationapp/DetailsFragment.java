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

import com.example.evaluationapp.databinding.FragmentDetailsBinding;
import com.example.evaluationapp.databinding.FragmentResultBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class DetailsFragment extends Fragment {

    FragmentDetailsBinding binding;
    IDetails am;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Details");
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.detailsView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.detailsView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.detailsView.getContext(), llm.getOrientation());
        binding.detailsView.addItemDecoration(dividerItemDecoration);


        am.getTeams(new MainActivity.Return() {
            @Override
            public void response(@NonNull String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Teams[] teams = gson.fromJson(response, Teams[].class);

                ArrayList<Teams> teamsArrayList = new ArrayList<>(Arrays.asList(teams));
                Log.d("demo", "response: "+teamsArrayList);

                ArrayList<Teams> newTeamList = new ArrayList<>();
                for(int i = 0; i<teamsArrayList.size(); i++){
                    Teams team = teamsArrayList.get(i);


                    if(team.getScores() == null){
                        newTeamList.add(new Teams(team.getTeamname(),"", 0));
                    }else{
                        ArrayList<Scores> scoresArrayList = team.getScores();
                        for(int j = 0; j<scoresArrayList.size(); j++){
                            Scores score = team.getScores().get(j);
                            newTeamList.add(new Teams(team.getTeamname(),score.getExaminername(), score.getScore() ));
                        }
                    }

                }
                Log.d("demo", "new list: " + newTeamList);

                binding.detailsView.setAdapter(new DetailsAdapter(newTeamList));
            }

            @Override
            public boolean showDialog() {
                return true;
            }

            @Override
            public void error(@NonNull String response) {
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
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
        if (context instanceof IDetails) {
            am = (IDetails) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IDetails {
        void sendAdminPortalView();
        void getTeams(MainActivity.Return response);
    }

}