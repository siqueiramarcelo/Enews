package com.ristana.e_news.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ristana.e_news.R;
import com.ristana.e_news.entity.news.ApiResponse;
import com.ristana.e_news.manager.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.api.news.apiNews;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    private ProgressBar intro_progress;
    private RelativeLayout activity_intro;
    private PrefManager prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prf= new PrefManager(getApplicationContext());

        initView();

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                IntroActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (prf.getString("LOGGED").contains("TRUE")){
                            checkAccount();
                        }else{
                            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        }, 2000);
    }
    private  void initView(){
        setContentView(R.layout.activity_intro);

        this.intro_progress=(ProgressBar) findViewById(R.id.intro_progress);
        this.activity_intro=(RelativeLayout) findViewById(R.id.activity_intro);
        intro_progress.setVisibility(View.VISIBLE);

    }
    private void checkAccount() {

            intro_progress.setVisibility(View.VISIBLE);
            Retrofit retrofit = newsClient.getClient();
            apiNews service = retrofit.create(apiNews.class);
            Call<ApiResponse> call = service.check(prf.getString("ID_USER"),prf.getString("TOKEN_USER"));
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    if (response.isSuccessful()){
                        if (response.body().getCode()==200){
                            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else if(response.body().getCode()==500){
                            logout();
                            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                    startActivity(intent);
                    intro_progress.setVisibility(View.INVISIBLE);

                }
            });

    }
    public      void logout(){
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.remove("ID_USER");
        prf.remove("SALT_USER");
        prf.remove("TOKEN_USER");
        prf.remove("NAME_USER");
        prf.remove("TYPE_USER");
        prf.remove("USERNAME_USER");
        prf.remove("LOGGED");
        Toast.makeText(getApplicationContext(),getString(R.string.message_logout_desibaled),Toast.LENGTH_LONG).show();
    }
    @Override
    public  void onPause(){
        super.onPause();
        finish();
    }
}
