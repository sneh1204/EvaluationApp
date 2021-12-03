package com.example.evaluationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.ExaminerViewBinding;
import com.example.evaluationapp.databinding.TeamViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExaminerAdapter extends RecyclerView.Adapter<ExaminerAdapter.UViewHolder>{

    ArrayList<User> users;
    ExaminerViewBinding binding;
    ViewGroup parent;

    public ExaminerAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ExaminerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.parent = parent;
        return new UViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, int position) {

        User user = users.get(position);
        holder.binding.examinerName.setText(user.getFullname());
    }
    @Override
    public int getItemCount() {
        return this.users.size();
    }


    public static class UViewHolder extends RecyclerView.ViewHolder {

        ExaminerViewBinding binding;

        public UViewHolder(@NonNull ExaminerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}

