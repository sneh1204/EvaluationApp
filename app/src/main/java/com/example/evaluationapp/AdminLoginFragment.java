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
import com.example.evaluationapp.databinding.FragmentAdminLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class AdminLoginFragment extends Fragment {

    FragmentAdminLoginBinding binding;

    String email, pass;

    IAdminLogin am;

    Auth0 auth0;


    @Override
    public void onResume() {
        super.onResume();
        if(am.getAdmin() != null){
            am.sendAdminPortalView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Admin Login");

        binding = FragmentAdminLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth0 = new Auth0(
                "YQIIAQIYXOJ8t7aXeR362GZdMMnIfkzI",
                "dev-9glxzlwm.us.auth0.com"
        );


        binding.loginButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        binding.examinerPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendLoginView();
            }
        });

        return view;
    }

    private void login() {
        WebAuthProvider.login(auth0)
                .withScheme("demo")
                .withScope("openid profile email")
                .start(getContext(), new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onFailure(@NonNull final AuthenticationException exception) {
                        Toast.makeText(getContext(), "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(@Nullable final Credentials credentials) {
                        String accessToken = credentials.getAccessToken();
                        String token = credentials.getIdToken();
                        showUserProfile(accessToken, token);
                        am.sendAdminPortalView();
                    }
                });
    }

    private void showUserProfile(String accessToken, String token) {

        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
                .start(new Callback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(UserProfile userProfile) {
                        Admin admin = new Admin(userProfile.getNickname(), userProfile.getEmail(), token);
                        am.setAdmin(admin);
                    }
                    @Override
                    public void onFailure(@NonNull AuthenticationException e) {
                        Log.d("demo", "onFailure: " );
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.ILogin) {
            am = (IAdminLogin) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IAdminLogin {

        void setAdmin(Admin admin);

        Admin getAdmin();

        void alert(String msg);

        void adminLogin(MainActivity.Return response, String... data);

        void sendAdminRegisterView();

        void sendAdminPortalView();

        void sendLoginView();

        void getAuthOLogin(MainActivity.Return response);

    }

}