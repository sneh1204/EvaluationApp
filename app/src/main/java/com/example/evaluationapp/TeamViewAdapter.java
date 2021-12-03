package com.example.evaluationapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.AdminteamViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamViewAdapter extends RecyclerView.Adapter<TeamViewAdapter.UViewHolder>{

    ArrayList<Teams> teams;
    AdminteamViewBinding binding;
    ViewGroup parent;

    public TeamViewAdapter(ArrayList<Teams> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdminteamViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        //holder.binding.scoreId.setText(String.valueOf(team.avgscore));
    }
    @Override
    public int getItemCount() {
        return this.teams.size();
    }


    public static class UViewHolder extends RecyclerView.ViewHolder {

        AdminteamViewBinding binding;

        public UViewHolder(@NonNull AdminteamViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
