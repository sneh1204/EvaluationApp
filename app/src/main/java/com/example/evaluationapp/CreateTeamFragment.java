package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentCreateTeamBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;


public class CreateTeamFragment extends Fragment {

    FragmentCreateTeamBinding binding;

    ICreateTeam am;

    String teamname;
    String city;
    int participants;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateTeam) {
            am = (ICreateTeam) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface ICreateTeam {


        void alert(String msg);

        void sendAdminsTeamView();

        void registerTeam(com.example.evaluationapp.MainActivity.Return response, String... data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Create Team");

        binding = FragmentCreateTeamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cancelButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendAdminsTeamView();
            }
        });

        binding.registerButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamname = binding.teamName.getText().toString();
                city = binding.city.getText().toString();
                participants = Integer.parseInt(binding.participants.getText().toString());


                if(teamname.isEmpty() || city.isEmpty() || (participants == 0)){
                    am.alert("Please enter all values for registering!");
                    return;
                }


                am.registerTeam(new MainActivity.Return() {
                    @Override
                    public void response(@NotNull String response) {
                        am.sendAdminsTeamView();
                    }

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public void error(@NotNull String response) {
                    }
                }, teamname, city, String.valueOf(participants));

            }
        });

        return view;
    }
}