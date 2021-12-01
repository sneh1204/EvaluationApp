package com.example.evaluationapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.TeamViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.UViewHolder>{

    ArrayList<Teams> teams;
    TeamViewBinding binding;
    ViewGroup parent;
    User user;

    public TeamsAdapter(User user, ArrayList<Teams> teams) {
        this.teams = teams;
        this.user = user;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = TeamViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.parent = parent;
        return new UViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, int position) {

        Teams team = teams.get(position);
        holder.binding.teamNameId.setText(team.getTeamname());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment surveyFragment = new SurveyFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, surveyFragment).commit();

            }
        });

    }
    @Override
    public int getItemCount() {
        return this.teams.size();
    }


    public static class UViewHolder extends RecyclerView.ViewHolder {

        TeamViewBinding binding;

        public UViewHolder(@NonNull TeamViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
