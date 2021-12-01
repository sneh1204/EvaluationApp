package com.example.evaluationapp;


import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.example.evaluationapp.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoginFragment.ILogin, SurveyFragment.ISurvey, ResultFragment.IResult, RegisterFragment.IRegister, TeamsFragment.ITeams {

    private final OkHttpClient client = new OkHttpClient();
    public static final String BASE_URL  = "http://10.0.2.2:3000/";//"https://aqueous-anchorage-82599.herokuapp.com/";

    ActivityMainBinding binding;

   // Auth0 account;

    ProgressDialog dialog;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  account = new Auth0("YQIIAQIYXOJ8t7aXeR362GZdMMnIfkzI", "dev-9glxzlwm.us.auth0.com");
        sendLoginView();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void sendLoginView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new LoginFragment())
                .commit();
    }


    public void sendTeamView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new TeamsFragment())
                .commit();
    }

    public void sendSurveyView(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new SurveyFragment())
                .commit();
    }

    @Override
    public void sendRegisterView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new RegisterFragment())
                .commit();
    }

    @Override
    public void sendResultView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new ResultFragment())
                .commit();
    }

    public void login(Return response, String... data){
        FormBody formBody = new FormBody.Builder()
                .add("email", data[0])
                .add("pass", data[1])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "auth/login")
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    public void register(Return response, String... data){
        FormBody formBody = new FormBody.Builder()
                .add("fullname", data[0])
                .add("address", data[1])
                .add("email", data[2])
                .add("pass", data[3])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "auth/signup")
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    public void getTeams(Return response) {
        Request request = new Request.Builder()
                .url(BASE_URL + "teams/getAll")
                .addHeader("x-jwt-token", user.getToken())
                .build();
        sendRequest(request, response);
    }

    public void profile(Return response){
        Request request = new Request.Builder()
                .url(BASE_URL + "profile/view")
                .addHeader("x-jwt-token", user.getToken())
                .build();
        sendRequest(request, response);
    }

    public void update(Return response, String ...data){
        FormBody formBody = new FormBody.Builder()
                .add("fullname", data[0])
                .add("score", data[1])
                .add("team", data[2])
                .add("avgscore", data[3])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "update/teamscore")
                .addHeader("x-jwt-token", user.getToken())
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    private void sendRequest(Request request, Return callback) {
        if(callback.showDialog()) toggleDialog(true, "Processing...");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if(callback.showDialog()) toggleDialog(false, null);
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(callback.showDialog()) toggleDialog(false, null);

                ResponseBody responseBody = response.body();

                String res_string;
                if (responseBody != null) {
                    res_string = responseBody.string();

                    if (response.isSuccessful()) {
                        runOnUiThread(() -> callback.response(res_string));
                    } else {
                        runOnUiThread(() -> {
                            try {
                                JSONObject jsonObject = new JSONObject(res_string);
                                if (jsonObject.has("message"))
                                    alert(jsonObject.getString("message"));
                            }catch (JSONException exc){
                            }
                        });
                        callback.error(res_string);
                    }
                }
            }
        });
    }

    @Override
    public void alert(String msg) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage(msg)
                .setPositiveButton("Okay", null)
                .show());
    }

    @Override
    public void goBack() {
        getSupportFragmentManager().popBackStack();
    }

    public void toggleDialog(boolean show, String msg) {
        if (show) {
            dialog = new ProgressDialog(this);
            if (msg == null)
                dialog.setMessage("Loading...");
            else
                dialog.setMessage(msg);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    interface Return{

        void response(@NotNull String response);

        void error(@NotNull String response);

        boolean showDialog();

    }


}