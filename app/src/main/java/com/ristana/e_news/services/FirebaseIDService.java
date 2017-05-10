package com.ristana.e_news.services;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ristana.e_news.R;
import com.ristana.e_news.api.news.apiNews;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.entity.news.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hsn on 01/03/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<ApiResponse> call = service.addDevice(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.v(TAG,"Hassan : "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.v(TAG,"Hassan : "+ t.getMessage().toString());
            }
        });
    }
}