package com.example.evaluationapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.DetailsViewBinding;
import com.example.evaluationapp.databinding.ResultViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.UViewHolder>{

    ArrayList<Teams> teams;
    DetailsViewBinding binding;
    ViewGroup parent;

    public DetailsAdapter(ArrayList<Teams> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DetailsViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        holder.binding.examiner.setText(team.getExaminerName());
        holder.binding.scoreId.setText(String.valueOf(team.getTeamScore()));

    }
    @Override
    public int getItemCount() {
        return this.teams.size();
    }


    public static class UViewHolder extends RecyclerView.ViewHolder {

        DetailsViewBinding binding;

        public UViewHolder(@NonNull DetailsViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
