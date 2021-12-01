package com.example.evaluationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.ResultViewBinding;
import com.example.evaluationapp.databinding.TeamViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.UViewHolder>{

    ArrayList<Teams> teams;
    ResultViewBinding binding;
    ViewGroup parent;
    User user;

    public ResultAdapter(User user, ArrayList<Teams> teams) {
        this.teams = teams;
        this.user = user;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ResultViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        holder.binding.scoreId.setText(String.valueOf(team.avgscore));
    }
    @Override
    public int getItemCount() {
        return this.teams.size();
    }


    public static class UViewHolder extends RecyclerView.ViewHolder {

        ResultViewBinding binding;

        public UViewHolder(@NonNull ResultViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
