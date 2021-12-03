package com.example.evaluationapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.evaluationapp.databinding.FragmentCreateExaminerBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class CreateExaminerFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    FragmentCreateExaminerBinding binding;

    ICreateExaminer am;

    String fullname, address, email, pass;

    String phoneNo = "4124824112";
    String message = "hello";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateExaminer) {
            am = (ICreateExaminer) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface ICreateExaminer {

        void setUser(User user);

        void alert(String msg);

        void goBack();

        void sendExaminerView();

        void register(com.example.evaluationapp.MainActivity.Return response, String... data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Create Examiner");

        binding = FragmentCreateExaminerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.cancelButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendExaminerView();
            }
        });

        binding.registerButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = binding.edittext.getText().toString();
                address = binding.edittext4.getText().toString();
                email = binding.edittext5.getText().toString();
                pass = binding.edittext6.getText().toString();

                if(email.isEmpty() || fullname.isEmpty() || address.isEmpty() || pass.isEmpty()){
                    am.alert("Please enter all values for registering!");
                    return;
                }


                am.register(new MainActivity.Return() {
                    @Override
                    public void response(@NotNull String response) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        User user = gson.fromJson(response, User.class);
                        am.setUser(user);
                        am.sendExaminerView();
                    }

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public void error(@NotNull String response) {
                    }
                }, fullname, address, email, pass);

            }
        });

        return view;
    }

}