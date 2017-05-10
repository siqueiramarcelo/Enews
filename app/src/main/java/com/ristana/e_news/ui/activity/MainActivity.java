package com.ristana.e_news.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ristana.e_news.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ristana.e_news.api.news.apiNews;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.api.weather.weatherClient;
import com.ristana.e_news.api.weather.apiWeather;
import com.ristana.e_news.config.Config;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.Category;
import com.ristana.e_news.entity.weather.weatherResponse;
import com.ristana.e_news.entity.weather.weatherResponseCurrent;
import com.ristana.e_news.manager.BlurTransformation;
import com.ristana.e_news.manager.GPSTracker;
import com.ristana.e_news.manager.PrefManager;
import com.ristana.e_news.services.AdsService;
import com.ristana.e_news.ui.widget.NewAppWidget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Handler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Target {
    String[] days ={"SUN","MON", "TUE", "WED", "THU", "FRI","SAT"};

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Category> mFragmentImages=new ArrayList<Category>();
    private ImageView weather_widget_image_view_icon;
    private ImageView weather_widget_image_view_day_1;
    private ImageView weather_widget_image_view_day_2;
    private ImageView weather_widget_image_view_day_3;
    private ImageView weather_widget_image_view_day_4;
    private ImageView weather_widget_image_view_day_5;
    private ImageView weather_widget_image_view_maker;

    private TextView weather_widget_text_view_city;
    private TextView weather_widget_text_view_temp;
    private TextView weather_widget_text_view_main;

    private TextView weather_widget_text_view_day_1;
    private TextView weather_widget_text_view_day_3;
    private TextView weather_widget_text_view_day_2;
    private TextView weather_widget_text_view_day_4;
    private TextView weather_widget_text_view_day_5;

    private TextView weather_widget_text_view_max_day_5;
    private TextView weather_widget_text_view_max_day_4;
    private TextView weather_widget_text_view_min_day_5;
    private TextView weather_widget_text_view_min_day_4;
    private TextView weather_widget_text_view_max_day_3;
    private TextView weather_widget_text_view_min_day_3;
    private TextView weather_widget_text_view_min_day_1;
    private TextView weather_widget_text_view_max_day_2;
    private TextView weather_widget_text_view_max_day_1;
    private TextView weather_widget_text_view_min_day_2;

    private FloatingSearchView  main_floating_search_view;
    private TabLayout           main_tab_layout;
    private ViewPager           main_view_pager;
    private NavigationView      main_navigation_view;
    private DrawerLayout        main_drawer_layout;

    private ImageView main_image_view_header;
    private GPSTracker gps;
    private PrefManager prf;
    private LinearLayout linearlayout_bottom;
    private CardView card_view_weather;
    private Button header_sign_in;
    private Button header_sign_up;
    private Button header_logout;
    private Button header_profile;
    private TextView header_text_view_name;
    private LinearLayout linearLayout_login;
    private LinearLayout linearLayout_user;
    private ViewPagerAdapter adapter;

    private CoordinatorLayout content_main;


    private static final int BACKGROUND_IMAGES_WIDTH = 360;
    private static final int BACKGROUND_IMAGES_HEIGHT = 360;
    private static final float BLUR_RADIUS = 10F;
    private final Handler handler = new Handler();

    private BlurTransformation blurTransformation;
    private int backgroundIndex;
    private Point backgroundImageTargetSize;
    private Category homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prf= new PrefManager(getApplicationContext());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        startService(new Intent(this, AdsService.class));//use to start the services

        initView();
        setupViewPager();
        initAction();
        findLocation();
        getCategoriesList();

        FirebaseMessaging.getInstance().subscribeToTopic("global");
        FirebaseInstanceId.getInstance().getToken();

        blurTransformation = new BlurTransformation(this, BLUR_RADIUS);
        backgroundImageTargetSize = calculateBackgroundImageSizeCroppedToScreenAspectRatio(
                getWindowManager().getDefaultDisplay());

    }


    public void getWeatherFiveDay(String longitude,String latitude,String units){
        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponse> call = service.getFiveDayWeather(latitude,longitude,"json",Config.KEY_APP_WEATHER,units);
        call.enqueue(new Callback<weatherResponse>() {
            @Override
            public void onResponse(Call<weatherResponse> call, Response<weatherResponse> response) {
               if (response.isSuccessful()){
                   if(response.body().getCod().equals("200")){
                       setWeatherFiveDay(response.body());
                   }
               }
            }
            @Override
            public void onFailure(Call<weatherResponse> call, Throwable t) {
               // Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getCurrentWeather(String longitude,String latitude,String units){
        Retrofit retrofit = weatherClient.getClient();
        apiWeather service = retrofit.create(apiWeather.class);
        retrofit2.Call<weatherResponseCurrent> call = service.getCurrentWeather(latitude,longitude,"json", Config.KEY_APP_WEATHER,units);
        call.enqueue(new Callback<weatherResponseCurrent>() {
            @Override
            public void onResponse(Call<weatherResponseCurrent> call, Response<weatherResponseCurrent> response) {
                if (response.isSuccessful()){
                       if (response.body().getCod().equals("200")){
                           setsetWeatherCurrent(response.body());
                       }
                }
            }

            @Override
            public void onFailure(Call<weatherResponseCurrent> call, Throwable t) {
               // Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void setsetWeatherCurrent(weatherResponseCurrent weatherResponseCurrent){
        String units="°F";

        if (!prf.getString("units").equals("metric") ){
            units="°F";
        }
        else if (!prf.getString("units").equals("imperial") ){
            units="°C";
        }
        this.weather_widget_text_view_city.setText(weatherResponseCurrent.getName());
        this.weather_widget_image_view_icon.setImageResource((weatherResponseCurrent.getWeather().get(0).getIconLocal()));

        this.weather_widget_text_view_temp.setText(weatherResponseCurrent.getMain().getTemp()+units);
        this.weather_widget_text_view_main.setText(weatherResponseCurrent.getWeather().get(0).getMain());

    }
    public void findLocation(){

        if (prf.getString("widget_display").equals("false")){
            card_view_weather.setVisibility(View.GONE);
        }else{
            card_view_weather.setVisibility(View.VISIBLE);
        }
        String latitude="";
        String longitude="";
        String units="imperial";
        gps = new GPSTracker(MainActivity.this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude()+"";
            longitude = gps.getLongitude()+"";
            prf.setString("latitude",latitude);
            prf.setString("longitude",longitude);
        }else{
            if (prf.getString("latitude").equals("") || prf.getString("longitude").equals("")){
                gps.showSettingsAlert();
                Toast.makeText(this, "no gps", Toast.LENGTH_SHORT).show();
            }else{
                latitude    =  prf.getString("latitude");
                longitude   =  prf.getString("longitude");
            }
        }

        if (!prf.getString("units").equals("") ){
            units=prf.getString("units");
        }
        // check if GPS enabled

        getCurrentWeather(longitude,latitude,units);
        getWeatherFiveDay(longitude,latitude,units);
    }
    public void setWeatherFiveDay(weatherResponse weather){


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Integer DAY_1=calendar.get(Calendar.DAY_OF_WEEK);
        weather_widget_image_view_day_1.setImageResource(weather.getList().get(1).getWeather().get(0).getIconLocal());
        weather_widget_text_view_day_1.setText(days[DAY_1-1]);
        weather_widget_text_view_max_day_1.setText(weather.getList().get(1).getTemp().getMax()+'°');
        weather_widget_text_view_min_day_1.setText(weather.getList().get(1).getTemp().getMin()+'°');

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Integer DAY_2=calendar.get(Calendar.DAY_OF_WEEK);
        weather_widget_image_view_day_2.setImageResource(weather.getList().get(2).getWeather().get(0).getIconLocal());
        weather_widget_text_view_day_2.setText(days[DAY_2-1]);
        weather_widget_text_view_max_day_2.setText(weather.getList().get(2).getTemp().getMax()+'°');
        weather_widget_text_view_min_day_2.setText(weather.getList().get(2).getTemp().getMin()+'°');

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Integer DAY_3=calendar.get(Calendar.DAY_OF_WEEK);
        weather_widget_image_view_day_3.setImageResource(weather.getList().get(3).getWeather().get(0).getIconLocal());
        weather_widget_text_view_day_3.setText(days[DAY_3-1]);
        weather_widget_text_view_max_day_3.setText(weather.getList().get(3).getTemp().getMax()+'°');
        weather_widget_text_view_min_day_3.setText(weather.getList().get(3).getTemp().getMin()+'°');

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Integer DAY_4=calendar.get(Calendar.DAY_OF_WEEK);
        weather_widget_image_view_day_4.setImageResource(weather.getList().get(4).getWeather().get(0).getIconLocal());
        weather_widget_text_view_day_4.setText(days[DAY_4-1]);
        weather_widget_text_view_max_day_4.setText(weather.getList().get(4).getTemp().getMax()+'°');
        weather_widget_text_view_min_day_4.setText(weather.getList().get(4).getTemp().getMin()+'°');


        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Integer DAY_5=calendar.get(Calendar.DAY_OF_WEEK);
        weather_widget_image_view_day_5.setImageResource(weather.getList().get(5).getWeather().get(0).getIconLocal());
        weather_widget_text_view_day_5.setText(days[DAY_5-1]);
        weather_widget_text_view_max_day_5.setText(weather.getList().get(5).getTemp().getMax()+'°');
        weather_widget_text_view_min_day_5.setText(weather.getList().get(5).getTemp().getMin()+'°');
    }
    public void initView(){
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        this.content_main=(CoordinatorLayout) findViewById(R.id.CoordinatorLayout_main);
        this.main_image_view_header=(ImageView) findViewById(R.id.main_image_view_header);

        this.weather_widget_image_view_icon=(ImageView)  findViewById(R.id.weather_widget_image_view_icon);
        this.weather_widget_image_view_day_1=(ImageView) findViewById(R.id.weather_widget_image_view_day_1);
        this.weather_widget_image_view_day_2=(ImageView) findViewById(R.id.weather_widget_image_view_day_2);
        this.weather_widget_image_view_day_3=(ImageView) findViewById(R.id.weather_widget_image_view_day_3);
        this.weather_widget_image_view_day_4=(ImageView) findViewById(R.id.weather_widget_image_view_day_4);
        this.weather_widget_image_view_day_5=(ImageView) findViewById(R.id.weather_widget_image_view_day_5);
        this.weather_widget_image_view_maker=(ImageView) findViewById(R.id.weather_widget_image_view_maker);

        this.weather_widget_text_view_city=(TextView) findViewById(R.id.weather_widget_text_view_city);
        this.weather_widget_text_view_temp=(TextView) findViewById(R.id.weather_widget_text_view_temp);
        this.weather_widget_text_view_main=(TextView) findViewById(R.id.weather_widget_text_view_main);

        this.weather_widget_text_view_day_1=(TextView) findViewById(R.id.weather_widget_text_view_day_1);
        this.weather_widget_text_view_day_2=(TextView) findViewById(R.id.weather_widget_text_view_day_2);
        this.weather_widget_text_view_day_3=(TextView) findViewById(R.id.weather_widget_text_view_day_3);
        this.weather_widget_text_view_day_4=(TextView) findViewById(R.id.weather_widget_text_view_day_4);
        this.weather_widget_text_view_day_5=(TextView) findViewById(R.id.weather_widget_text_view_day_5);

        this.weather_widget_text_view_max_day_5=(TextView) findViewById(R.id.weather_widget_text_view_max_day_5);
        this.weather_widget_text_view_min_day_5=(TextView) findViewById(R.id.weather_widget_text_view_min_day_5);
        this.weather_widget_text_view_max_day_4=(TextView) findViewById(R.id.weather_widget_text_view_max_day_4);
        this.weather_widget_text_view_min_day_4=(TextView) findViewById(R.id.weather_widget_text_view_min_day_4);
        this.weather_widget_text_view_max_day_3=(TextView) findViewById(R.id.weather_widget_text_view_max_day_3);
        this.weather_widget_text_view_min_day_3=(TextView) findViewById(R.id.weather_widget_text_view_min_day_3);
        this.weather_widget_text_view_max_day_2=(TextView) findViewById(R.id.weather_widget_text_view_max_day_2);
        this.weather_widget_text_view_min_day_2=(TextView) findViewById(R.id.weather_widget_text_view_min_day_2);
        this.weather_widget_text_view_max_day_1=(TextView) findViewById(R.id.weather_widget_text_view_max_day_1);
        this.weather_widget_text_view_min_day_1=(TextView) findViewById(R.id.weather_widget_text_view_min_day_1);


        this.main_floating_search_view =    (FloatingSearchView) findViewById(R.id.main_floating_search_view);
        this.main_tab_layout           =    (TabLayout) findViewById(R.id.main_tab_layout);
        this.main_view_pager           =    (ViewPager) findViewById(R.id.main_view_pager);
        main_view_pager.setOffscreenPageLimit(100);

        this.main_navigation_view      =    (NavigationView) findViewById(R.id.main_navigation_view);
        this.main_drawer_layout        =    (DrawerLayout) findViewById(R.id.main_drawer_layout);


        this.main_tab_layout.setupWithViewPager(this.main_view_pager);
        this.main_navigation_view.setNavigationItemSelectedListener(this);
        this.main_floating_search_view.attachNavigationDrawerToMenuButton(this.main_drawer_layout);
        this.linearlayout_bottom=(LinearLayout) findViewById(R.id.linearlayout_bottom);
        this.card_view_weather=(CardView) findViewById(R.id.card_view_weather);

        View headerview = main_navigation_view.getHeaderView(0);
        this.header_sign_in=(Button) headerview.findViewById(R.id.header_sign_in);
        this.header_sign_up=(Button) headerview.findViewById(R.id.header_sign_up);
        this.header_logout =(Button) headerview.findViewById(R.id.header_logout);
        this.header_profile=(Button) headerview.findViewById(R.id.header_profile);
        this.header_text_view_name=(TextView) headerview.findViewById(R.id.header_text_view_name);
        this.linearLayout_login=(LinearLayout) headerview.findViewById(R.id.linearLayout_login);
        this.linearLayout_user=(LinearLayout) headerview.findViewById(R.id.linearLayout_user);
    }

    public void initAction(){
        this.main_floating_search_view.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Intent intent= new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("query",currentQuery);
                startActivity(intent);
            }
        });
        this.weather_widget_image_view_maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    if(!gps.canGetLocation()){
                        gps.showSettingsAlert();
                    }
                    findLocation();
                }


            }});
        card_view_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearlayout_bottom.getVisibility() == View.GONE) {
                    linearlayout_bottom.animate()
                            .translationY(0).alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    linearlayout_bottom.setVisibility(View.VISIBLE);
                                    linearlayout_bottom.setAlpha(0.0f);
                                }
                            });
                } else {
                    linearlayout_bottom.animate()
                            .translationY(0).alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    linearlayout_bottom.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
        this.header_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);            }
        });
        this.header_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        this.header_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        this.header_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });
        this.main_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    updateWindowBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.home_action:
                main_view_pager.setCurrentItem(0);
                break;
            case R.id.setting_action:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
            case R.id.saved_action:
                startActivity(new Intent(MainActivity.this,SavedActivity.class));
                break;
            case R.id.policy_action:
                startActivity(new Intent(MainActivity.this,PolicyActivity.class));
                break;
            case R.id.contact_us_action:
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
                break;
            case R.id.about_us_action:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;
            case R.id.share_action:
                String shareBody = getString(R.string.app_name)+" "+getString(R.string.url_app_google_play);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setupViewPager() {

        adapter.addFragment(new HomeFragment(),"");

        main_view_pager.setAdapter(adapter);
        main_view_pager.setCurrentItem(0);
        createTabIcons();
    }

    private void createTabIcons() {

        ImageView tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        main_tab_layout.getTabAt(0).setCustomView(tabOne);


    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    if (requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findLocation();
            }
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        try {
            Intent updateWidget = new Intent(getApplicationContext(), NewAppWidget.class); // Widget.class is your widget class
            updateWidget.setAction("update_widget");
            PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
            pending.send();
        }catch (PendingIntent.CanceledException e){

        }
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","false");
    }
    @Override
    public void onResume(){
        super.onResume();

        if (prf.getString("LOGGED").toString().equals("TRUE")){
            linearLayout_login.setVisibility(View.GONE);
            linearLayout_user.setVisibility(View.VISIBLE);
            header_text_view_name.setText(prf.getString("NAME_USER").toString());
        }else{
            linearLayout_login.setVisibility(View.VISIBLE);
            linearLayout_user.setVisibility(View.GONE);
            header_text_view_name.setText("");
        }
        if (prf.getString("widget_display").equals("false")){
            card_view_weather.setVisibility(View.GONE);
        }else{

            card_view_weather.setVisibility(View.VISIBLE);
            findLocation();
        }

        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","true");
    }
    @Override
    public void onStart(){
        super.onStart();
        if (prf.getString("LOGGED").toString().equals("TRUE")){
            linearLayout_login.setVisibility(View.GONE);
            linearLayout_user.setVisibility(View.VISIBLE);
            header_text_view_name.setText(prf.getString("NAME_USER").toString());
        }else{
            linearLayout_login.setVisibility(View.VISIBLE);
            linearLayout_user.setVisibility(View.GONE);
            header_text_view_name.setText("");
        }
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","true");
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
        if (prf.getString("LOGGED").toString().equals("TRUE")){
            linearLayout_login.setVisibility(View.GONE);
            linearLayout_user.setVisibility(View.VISIBLE);
            header_text_view_name.setText(prf.getString("NAME_USER").toString());
        }else{
            linearLayout_login.setVisibility(View.VISIBLE);
            linearLayout_user.setVisibility(View.GONE);
            header_text_view_name.setText("");
        }
        Toast.makeText(getApplicationContext(),getString(R.string.message_logout),Toast.LENGTH_LONG).show();
    }
    public void getCategoriesList(){
        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<List<Category>> call = service.categorriesList();
        call.enqueue(new Callback<List<Category>>() {

            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        if (response.body().get(i).getId()!=0){
                            Bundle bundle = new Bundle();
                            String myMessage =response.body().get(i).getTitle();
                            bundle.putString("id_category", response.body().get(i).getId()+"" );

                            PageFragment pageFragment=  new PageFragment();

                            pageFragment.setArguments(bundle);



                            adapter.addFragment(pageFragment,response.body().get(i).getTitle());
                            adapter.notifyDataSetChanged();
                            mFragmentImages.add(response.body().get(i));
                            createTabIcons();
                        }else{
                            homeFragment=response.body().get(i);
                            updateWindowBackground(0);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Snackbar snackbar = Snackbar
                        .make(content_main, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCategoriesList();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        changeBackground(new BitmapDrawable(getResources(), bitmap));

    }
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        getWindow().setBackgroundDrawable(errorDrawable);

    }
    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
    private String getUrlToTheImage(int position) {
        if (position==0){
            final String imageUrl = homeFragment.getImage();
            return imageUrl;

        }else{
            final String imageUrl = mFragmentImages.get(position-1).getImage();
            return imageUrl;
        }
    }
    private void updateWindowBackground(int position) {

            String url = getUrlToTheImage(position);
            Picasso.with(this).load(url)
                    .resize(backgroundImageTargetSize.x, backgroundImageTargetSize.y).centerCrop()
                    .transform(blurTransformation).error(R.drawable.bg_login).into(this);

    }
    private void changeBackground(Drawable drawable) {
        View decorView = getWindow().getDecorView();
        Drawable oldBackgroundDrawable = decorView.getBackground();
        TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[]{oldBackgroundDrawable, drawable});
        setBackgroundCompat(decorView, transitionDrawable);
        transitionDrawable.startTransition(1000);
    }
    private static void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
    }
    private static Point calculateBackgroundImageSizeCroppedToScreenAspectRatio(Display display) {
        final Point screenSize = new Point();
        getSizeCompat(display, screenSize);
        int scaledWidth = (int) (((double) BACKGROUND_IMAGES_HEIGHT * screenSize.x) / screenSize.y);
        int croppedWidth = Math.min(scaledWidth, BACKGROUND_IMAGES_WIDTH);
        int scaledHeight = (int) (((double) BACKGROUND_IMAGES_WIDTH * screenSize.y) / screenSize.x);
        int croppedHeight = Math.min(scaledHeight, BACKGROUND_IMAGES_HEIGHT);
        return new Point(croppedWidth, croppedHeight);
    }
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static void getSizeCompat(Display display, Point screenSize) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(screenSize);
        } else {
            screenSize.x = display.getWidth();
            screenSize.y = display.getHeight();
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","false");
    }
    @Override
    public  void onRestart(){
        super.onRestart();
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","true");
    }
}