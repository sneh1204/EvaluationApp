package com.example.evaluationapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluationapp.databinding.FragmentRegisterAdminBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class RegisterAdminFragment extends Fragment {

    FragmentRegisterAdminBinding binding;

    IAdminRegister am;

    String fullname, email, pass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAdminRegister) {
            am = (IAdminRegister) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IAdminRegister {

        void goBack();

        void adminRegister(com.example.evaluationapp.MainActivity.Return response, String... data);

        void setAdmin(Admin admin);

        Admin getAdmin();

        void alert(String msg);

        void sendAdminPortalView();

        void sendAdminLoginView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("AdminRegister");

        binding = FragmentRegisterAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cancelButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.goBack();
            }
        });

        binding.registerButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = binding.edittext.getText().toString();
                email = binding.edittext5.getText().toString();
                pass = binding.edittext6.getText().toString();

                if(email.isEmpty() || fullname.isEmpty() || pass.isEmpty()){
                    am.alert("Please enter all values for registering!");
                    return;
                }


                am.adminRegister(new MainActivity.Return() {
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
                }, fullname, email, pass);

            }
        });

        binding.cancelButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendAdminLoginView();
            }
        });

        return view;
    }
}