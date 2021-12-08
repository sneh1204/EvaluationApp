package com.example.evaluationapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.evaluationapp.databinding.FragmentCreateExaminerBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CreateExaminerFragment extends Fragment {

    FragmentCreateExaminerBinding binding;

    ICreateExaminer am;

    String fullname, address, email, pass, phone;

    String ACCOUNT_SID = "AC24dc106e7f4e42c14f6d1cbfe29c10ae";
    String AUTH_TOKEN = "6121d40067176a88ee79401a391157fa";
    String TWILIO_NUMBER = "+13252406430";

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
                phone = binding.phoneNumber.getText().toString();

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
                        //am.setUser(user);
                        am.sendExaminerView();
                        sendSMS(email , pass, phone);
                    }

                    @Override
                    public boolean showDialog() {
                        return true;
                    }

                    @Override
                    public void error(@NotNull String response) {
                    }
                }, fullname, address, email, pass, phone);

            }
        });

        return view;
    }

    public void  sendSMS(String email , String pass, String phone){

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
        String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

        RequestBody body = new FormBody.Builder()
                .add("From", TWILIO_NUMBER)
                .add("To", phone)
                .add("Body", "For evaluation survey your username : " + email + " and password : "+ pass)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", base64EncodedCredentials)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("demo", "error: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("demo", "sendSms: "+ response.body().string());
            }
        });
    }

}