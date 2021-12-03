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
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.example.evaluationapp.databinding.FragmentAdminLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;


public class AdminLoginFragment extends Fragment {

    FragmentAdminLoginBinding binding;

    String email, pass;

    IAdminLogin am;

    Auth0 auth0;

    public static final String EXTRA_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS";
    public static final String EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN";

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

        binding.createNewAccountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendAdminRegisterView();
            }
        });

//        binding.loginButtonId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                login();
//            }
//        });

       // auth0 = new Auth0(getContext());

        binding.loginButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.emailTextFieldId.getText().toString();
                pass = binding.passwordTextFieldId.getText().toString();

                if(email.isEmpty() || pass.isEmpty()){
                    am.alert("Please enter all values!");
                    return;
                }

                am.adminLogin(new MainActivity.Return() {
                    @Override
                    public void response(@NotNull String response) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Admin admin = gson.fromJson(response, Admin.class);

                        am.setAdmin(admin);
                        am.sendAdminPortalView();
                    }

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public void error(@NotNull String response) {
                    }

                }, email, pass);

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

//    private void login() {
//        WebAuthProvider.login(auth0)
//                .withScheme("https")
//                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
//                .start(getContext(), new Callback<Credentials, AuthenticationException>() {
//
//                    @Override
//                    public void onFailure(@NonNull final AuthenticationException exception) {
//                        Log.d("demo", "onFailure: " + exception.getMessage() );
//                        Toast.makeText(getContext(), "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(@Nullable final Credentials credentials) {
//                        String token = credentials.getAccessToken();
//                        Log.d("demo", "onSuccess: " + token);
//                    }
//                });
//    }

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

    }

}