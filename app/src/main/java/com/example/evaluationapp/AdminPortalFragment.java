package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.example.evaluationapp.databinding.FragmentAdminPortalBinding;
import com.example.evaluationapp.databinding.FragmentTeamsBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;


public class AdminPortalFragment extends Fragment {

    FragmentAdminPortalBinding binding;

    IAdminPortal am;

    Admin admin;

    Auth0 auth0;

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
         void sendDetailsView();
         void sendLoginView();
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

        auth0 = new Auth0(
                "YQIIAQIYXOJ8t7aXeR362GZdMMnIfkzI",
                "dev-9glxzlwm.us.auth0.com"
        );

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

        binding.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendDetailsView();
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return view;

    }

    public void logout(){
        WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(getContext(), new Callback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("demo", "onSuccess: ");
                        am.sendLoginView();
                    }

                    @Override
                    public void onFailure(@NonNull AuthenticationException e) {
                        Log.d("demo", "onFailure: ");
                    }
                });
    }


}