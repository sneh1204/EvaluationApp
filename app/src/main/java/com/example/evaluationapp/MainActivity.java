package com.example.evaluationapp;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.evaluationapp.model.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoginFragment.ILogin, SurveyFragment.ISurvey, ResultFragment.IResult {

    private final OkHttpClient client = new OkHttpClient();
    public static final String BASE_URL  = "https://mysterious-beach-05426.herokuapp.com/"; // https://mysterious-beach-05426.herokuapp.com/ or http://10.0.2.2:3000/

    ProgressDialog dialog;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void sendSurveyView(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new SurveyFragment())
                .commit();
    }

    @Override
    public void sendResultView(int total_score) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, ResultFragment.newInstance(total_score))
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