package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentAdminPortalBinding;
import com.example.evaluationapp.databinding.FragmentTeamsBinding;


public class AdminPortalFragment extends Fragment {

    FragmentAdminPortalBinding binding;

    IAdminPortal am;

    Admin admin;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAdminPortal) {
            am = (IAdminPortal) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IAdminPortal {

         void setAdmin(Admin admin);
         Admin getAdmin();
         void alert(String msg);
         void sendAdminLoginView();
         void sendExaminerView();
         void sendAdminsTeamView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Admin Portal");

        binding = FragmentAdminPortalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        admin = am.getAdmin();
        binding.createExaminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendExaminerView();
            }
        });

        binding.createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendAdminsTeamView();
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.setAdmin(null);
                am.sendAdminLoginView();
            }
        });

        return view;

    }
}