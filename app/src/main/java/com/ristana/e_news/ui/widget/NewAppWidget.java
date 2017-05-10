package com.ristana.e_news.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.ristana.e_news.R;
import com.ristana.e_news.api.weather.apiWeather;
import com.ristana.e_news.api.weather.weatherClient;
import com.ristana.e_news.config.Config;
import com.ristana.e_news.entity.weather.weatherResponse;
import com.ristana.e_news.entity.weather.weatherResponseCurrent;
import com.ristana.e_news.manager.GPSTracker;
import com.ristana.e_news.manager.PrefManager;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String TAG = NewAppWidget.class.getSimpleName();

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "start update");
        CharSequence widgetText = NewAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        String latitude="";
        String longitude="";

        GPSTracker gps = new GPSTracker(context);

        PrefManager prf= new PrefManager(context);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude()+"";
            longitude = gps.getLongitude()+"";
            prf.setString("latitude",latitude);
            prf.setString("longitude",longitude);
        }else{
            if (prf.getString("latitude").equals("") || prf.getString("longitude").equals("")){
            }else{
                latitude    =  prf.getString("latitude");
                longitude   =  prf.getString("longitude");
            }
        }
        String units="imperial";
        prf= new PrefManager(context);
        if (!prf.getString("units").equals("") ){
            units=prf.getString("units");
        }
        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponse> call = service.getFiveDayWeather(latitude,longitude, "json", Config.KEY_APP_WEATHER, units);
        call.enqueue(new Callback<weatherResponse>() {
            @Override
            public void onResponse(Call<weatherResponse> call, Response<weatherResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCod().equals("200")) {
                        String[] days ={"SUN","MON", "TUE", "WED", "THU", "FRI","SAT"};
                        Log.d(TAG, "Ok");
                        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

                        Calendar calendar = Calendar.getInstance();
                        Date today = calendar.getTime();
                        weatherResponse weather = response.body();

                        Log.d(TAG, weather.getCity().getName());
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_1 = calendar.get(Calendar.DAY_OF_WEEK);



                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_1, days[DAY_1 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_1, weather.getList().get(1).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_1, weather.getList().get(1).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_1,weather.getList().get(1).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_2 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_2, days[DAY_2 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_2, weather.getList().get(2).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_2, weather.getList().get(2).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_2,weather.getList().get(2).getWeather().get(0).getIconLocalP());

                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_3 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_3, days[DAY_3 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_3, weather.getList().get(3).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_3, weather.getList().get(3).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_3,weather.getList().get(3).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_4 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_4, days[DAY_4 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_4, weather.getList().get(4).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_4, weather.getList().get(4).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_4,weather.getList().get(4).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_5 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_5, days[DAY_5 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_5, weather.getList().get(5).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_5, weather.getList().get(5).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_5,weather.getList().get(5).getWeather().get(0).getIconLocalP());

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                        mgr.updateAppWidget(cn, updateViews);
                    }
                }
            }

            @Override
            public void onFailure(Call<weatherResponse> call, Throwable t) {
                Log.d(TAG, "No");
            }
        });


        Retrofit retrofit_one = weatherClient.getClient();
        apiWeather service_one = retrofit_one.create(apiWeather.class);
        retrofit2.Call<weatherResponseCurrent> call_one = service_one.getCurrentWeather(latitude,longitude,"json",Config.KEY_APP_WEATHER,units);
        call_one.enqueue(new Callback<weatherResponseCurrent>() {
            @Override
            public void onResponse(Call<weatherResponseCurrent> call, Response<weatherResponseCurrent> response) {
                if (response.isSuccessful()){
                    if (response.body().getCod().equals("200")){
                        weatherResponseCurrent responseCurrent= response.body();
                        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

                        String units="°F";
                        PrefManager prf= new PrefManager(context);
                        if (!prf.getString("units").equals("metric") ){
                            units="°F";
                        }
                        else if (!prf.getString("units").equals("imperial") ){
                            units="°C";
                        }


                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_temp,responseCurrent.getMain().getTemp()+units);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_main,responseCurrent.getWeather().get(0).getMain());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_icon,responseCurrent.getWeather().get(0).getIconLocalP());

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                        mgr.updateAppWidget(cn, updateViews);
                    }
                }
            }

            @Override
            public void onFailure(Call<weatherResponseCurrent> call, Throwable t) {

            }
        });



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onReceive(final Context context, Intent widgetIntent) {


        String latitude="";
        String longitude="";

        GPSTracker gps = new GPSTracker(context);

        PrefManager prf= new PrefManager(context);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude()+"";
            longitude = gps.getLongitude()+"";
            prf.setString("latitude",latitude);
            prf.setString("longitude",longitude);
        }else{
            if (prf.getString("latitude").equals("") || prf.getString("longitude").equals("")){
            }else{
                latitude    =  prf.getString("latitude");
                longitude   =  prf.getString("longitude");
            }
        }
        String units="imperial";
        prf= new PrefManager(context);
        if (!prf.getString("units").equals("") ){
            units=prf.getString("units");
        }
        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponse> call = service.getFiveDayWeather(latitude,longitude, "json", Config.KEY_APP_WEATHER, units);
        call.enqueue(new Callback<weatherResponse>() {
            @Override
            public void onResponse(Call<weatherResponse> call, Response<weatherResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCod().equals("200")) {
                        String[] days ={"SUN","MON", "TUE", "WED", "THU", "FRI","SAT"};
                        Log.d(TAG, "Ok");
                        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

                        Calendar calendar = Calendar.getInstance();
                        Date today = calendar.getTime();
                        weatherResponse weather = response.body();

                        Log.d(TAG, weather.getCity().getName());
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_1 = calendar.get(Calendar.DAY_OF_WEEK);



                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_1, days[DAY_1 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_1, weather.getList().get(1).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_1, weather.getList().get(1).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_1,weather.getList().get(1).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_2 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_2, days[DAY_2 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_2, weather.getList().get(2).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_2, weather.getList().get(2).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_2,weather.getList().get(2).getWeather().get(0).getIconLocalP());

                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_3 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_3, days[DAY_3 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_3, weather.getList().get(3).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_3, weather.getList().get(3).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_3,weather.getList().get(3).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_4 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_4, days[DAY_4 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_4, weather.getList().get(4).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_4, weather.getList().get(4).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_4,weather.getList().get(4).getWeather().get(0).getIconLocalP());


                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Integer DAY_5 = calendar.get(Calendar.DAY_OF_WEEK);

                        updateViews.setTextViewText(R.id.weather_widget_text_view_day_5, days[DAY_5 - 1]);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_5, weather.getList().get(5).getTemp().getMax() + '°');
                        updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_5, weather.getList().get(5).getTemp().getMin() + '°');
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_day_5,weather.getList().get(5).getWeather().get(0).getIconLocalP());

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                        mgr.updateAppWidget(cn, updateViews);
                    }
                }
            }

            @Override
            public void onFailure(Call<weatherResponse> call, Throwable t) {
                Log.d(TAG, "No");
            }
        });


        Retrofit retrofit_one = weatherClient.getClient();
        apiWeather service_one = retrofit_one.create(apiWeather.class);
        retrofit2.Call<weatherResponseCurrent> call_one = service_one.getCurrentWeather(latitude,longitude,"json",Config.KEY_APP_WEATHER,units);
        call_one.enqueue(new Callback<weatherResponseCurrent>() {
            @Override
            public void onResponse(Call<weatherResponseCurrent> call, Response<weatherResponseCurrent> response) {
                if (response.isSuccessful()){
                    if (response.body().getCod().equals("200")){
                        weatherResponseCurrent responseCurrent= response.body();
                        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);


                        String units="°F";
                        PrefManager prf= new PrefManager(context);
                        if (!prf.getString("units").equals("metric") ){
                            units="°F";
                        }
                        else if (!prf.getString("units").equals("imperial") ){
                            units="°C";
                        }


                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_temp,responseCurrent.getMain().getTemp()+units);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_main,responseCurrent.getWeather().get(0).getMain());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_icon,responseCurrent.getWeather().get(0).getIconLocalP());

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                        mgr.updateAppWidget(cn, updateViews);
                    }
                }
            }

            @Override
            public void onFailure(Call<weatherResponseCurrent> call, Throwable t) {

            }
        });


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
        final String action = widgetIntent.getAction();
        Log.d(TAG, "action received: " + action);
        super.onReceive(context, widgetIntent);
    }
    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "updating app widget");

        String latitude="";
        String longitude="";

        GPSTracker gps = new GPSTracker(context);

        PrefManager prf= new PrefManager(context);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude()+"";
            longitude = gps.getLongitude()+"";
            prf.setString("latitude",latitude);
            prf.setString("longitude",longitude);
        }else{
            if (prf.getString("latitude").equals("") || prf.getString("longitude").equals("")){
            }else{
                latitude    =  prf.getString("latitude");
                longitude   =  prf.getString("longitude");
            }
        }
        String units="imperial";
        prf= new PrefManager(context);
        if (!prf.getString("units").equals("") ){
            units=prf.getString("units");
        }

        Retrofit retrofit = weatherClient.getClient();
            apiWeather service = retrofit.create(apiWeather.class);
            retrofit2.Call<weatherResponse> call = service.getFiveDayWeather(latitude,longitude, "json",Config.KEY_APP_WEATHER, units);
            call.enqueue(new Callback<weatherResponse>() {
                @Override
                public void onResponse(Call<weatherResponse> call, Response<weatherResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCod().equals("200")) {
                            String[] days ={"SUN","MON", "TUE", "WED", "THU", "FRI","SAT"};

                            Log.d(TAG, "Ok");
                            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

                            Calendar calendar = Calendar.getInstance();
                            Date today = calendar.getTime();
                            weatherResponse weather = response.body();

                            Log.d(TAG, weather.getCity().getName());
                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_1 = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.d(TAG, DAY_1+"");

                            updateViews.setTextViewText(R.id.weather_widget_text_view_day_1, days[DAY_1 - 1]);

                            updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_1, weather.getList().get(1).getTemp().getMax() + '°');
                            updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_1, weather.getList().get(1).getTemp().getMin() + '°');
                            updateViews.setImageViewResource(R.id.weather_widget_image_view_day_1,weather.getList().get(1).getWeather().get(0).getIconLocalP());


                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_2 = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.d(TAG, DAY_2+"");

                            updateViews.setTextViewText(R.id.weather_widget_text_view_day_2, days[DAY_2 - 1]);
                            updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_2, weather.getList().get(2).getTemp().getMax() + '°');
                            updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_2, weather.getList().get(2).getTemp().getMin() + '°');
                            updateViews.setImageViewResource(R.id.weather_widget_image_view_day_2,weather.getList().get(2).getWeather().get(0).getIconLocalP());


                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_3 = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.d(TAG, DAY_3+"");

                            updateViews.setTextViewText(R.id.weather_widget_text_view_day_3, days[DAY_3 - 1]);
                            updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_3, weather.getList().get(3).getTemp().getMax() + '°');
                            updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_3, weather.getList().get(3).getTemp().getMin() + '°');
                            updateViews.setImageViewResource(R.id.weather_widget_image_view_day_3,weather.getList().get(3).getWeather().get(0).getIconLocalP());



                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_4 = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.d(TAG, DAY_4+"");

                            updateViews.setTextViewText(R.id.weather_widget_text_view_day_4, days[DAY_4 - 1]);
                            updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_4, weather.getList().get(4).getTemp().getMax() + '°');
                            updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_4, weather.getList().get(4).getTemp().getMin() + '°');
                            updateViews.setImageViewResource(R.id.weather_widget_image_view_day_4,weather.getList().get(4).getWeather().get(0).getIconLocalP());


                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Integer DAY_5 = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.d(TAG, DAY_5+"");

                            updateViews.setTextViewText(R.id.weather_widget_text_view_day_5, days[DAY_5 - 1]);
                            updateViews.setTextViewText(R.id.weather_widget_text_view_max_day_5, weather.getList().get(5).getTemp().getMax() + '°');
                            updateViews.setTextViewText(R.id.weather_widget_text_view_min_day_5, weather.getList().get(5).getTemp().getMin() + '°');
                            updateViews.setImageViewResource(R.id.weather_widget_image_view_day_5,weather.getList().get(5).getWeather().get(0).getIconLocalP());

                            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                            final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                            mgr.updateAppWidget(cn, updateViews);
                        }
                    }
                }

                @Override
                public void onFailure(Call<weatherResponse> call, Throwable t) {
                    Log.d(TAG, "No");
                }
            });

        Retrofit retrofit_one = weatherClient.getClient();
        apiWeather service_one = retrofit_one.create(apiWeather.class);
        retrofit2.Call<weatherResponseCurrent> call_one = service_one.getCurrentWeather(latitude,longitude,"json",Config.KEY_APP_WEATHER,units);
        call_one.enqueue(new Callback<weatherResponseCurrent>() {
            @Override
            public void onResponse(Call<weatherResponseCurrent> call, Response<weatherResponseCurrent> response) {
                if (response.isSuccessful()){
                    if (response.body().getCod().equals("200")){
                        weatherResponseCurrent responseCurrent= response.body();
                        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

                        String units="°F";
                        PrefManager prf= new PrefManager(context);
                        if (!prf.getString("units").equals("metric") ){
                            units="°F";
                        }
                        else if (!prf.getString("units").equals("imperial") ){
                            units="°C";
                        }

                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_temp,responseCurrent.getMain().getTemp()+units);
                        updateViews.setTextViewText(R.id.weather_widget_text_view_main,responseCurrent.getWeather().get(0).getMain());
                        updateViews.setTextViewText(R.id.weather_widget_text_view_city,responseCurrent.getName());
                        updateViews.setImageViewResource(R.id.weather_widget_image_view_icon,responseCurrent.getWeather().get(0).getIconLocalP());

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, NewAppWidget.class);
                        mgr.updateAppWidget(cn, updateViews);
                    }
                }
            }

            @Override
            public void onFailure(Call<weatherResponseCurrent> call, Throwable t) {

            }
        });

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

