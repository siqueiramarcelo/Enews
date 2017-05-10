package com.ristana.e_news.ui.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ristana.e_news.R;
import com.ristana.e_news.manager.PrefManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch switch_button_weather_widget;
    private PrefManager prf;
    private TextView text_view_temp_unit_c;
    private TextView text_view_temp_unit_f;
    private TextView text_view_temp_unit_title;
    private Switch switch_button_notification;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prf= new PrefManager(getApplicationContext());

        initView();
        setValues();
        initAction();
    }
    public void initView(){
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
        this.switch_button_weather_widget=(Switch) findViewById(R.id.switch_button_weather_widget);
        this.text_view_temp_unit_title=(TextView) findViewById(R.id.text_view_temp_unit_title);
        this.text_view_temp_unit_c=(TextView) findViewById(R.id.text_view_temp_unit_c);
        this.text_view_temp_unit_f=(TextView) findViewById(R.id.text_view_temp_unit_f);
        this.switch_button_notification=(Switch) findViewById(R.id.switch_button_notification);
    }
    public void setValues(){
        if (prf.getString("notifications").equals("false")){
            this.switch_button_notification.setChecked(false);
        }else{
            this.switch_button_notification.setChecked(true);
        }
        if (prf.getString("widget_display").equals("false")){
            this.switch_button_weather_widget.setChecked(false);
        }else{
            this.switch_button_weather_widget.setChecked(true);
        }
        if (prf.getString("units").equals("metric")){
            text_view_temp_unit_c.setTextColor(getResources().getColor(R.color.white));
            text_view_temp_unit_f.setTextColor(getResources().getColor(R.color.black));
            text_view_temp_unit_c.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            text_view_temp_unit_f.setBackgroundColor(getResources().getColor(R.color.graycolor));
            text_view_temp_unit_title.setText(getResources().getString(R.string.units_setting_title)+" °C");

        }else{
            text_view_temp_unit_f.setTextColor(getResources().getColor(R.color.white));
            text_view_temp_unit_c.setTextColor(getResources().getColor(R.color.black));
            text_view_temp_unit_f.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            text_view_temp_unit_c.setBackgroundColor(getResources().getColor(R.color.graycolor));
            text_view_temp_unit_title.setText(getResources().getString(R.string.units_setting_title)+" °F");

        }
    }
    public void initAction(){
        this.text_view_temp_unit_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_view_temp_unit_c.setTextColor(getResources().getColor(R.color.white));
                text_view_temp_unit_f.setTextColor(getResources().getColor(R.color.black));
                text_view_temp_unit_c.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                text_view_temp_unit_f.setBackgroundColor(getResources().getColor(R.color.graycolor));
                prf.setString("units","metric");
                text_view_temp_unit_title.setText(getResources().getString(R.string.units_setting_title)+" °C");

            }
        });
        this.text_view_temp_unit_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_view_temp_unit_f.setTextColor(getResources().getColor(R.color.white));
                text_view_temp_unit_c.setTextColor(getResources().getColor(R.color.black));
                text_view_temp_unit_f.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                text_view_temp_unit_c.setBackgroundColor(getResources().getColor(R.color.graycolor));
                prf.setString("units","imperial");
                text_view_temp_unit_title.setText(getResources().getString(R.string.units_setting_title)+" °F");
            }
        });
        
        this.switch_button_weather_widget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    prf.setString("widget_display","true");
                }else{
                    prf.setString("widget_display","false");
                }
            }
        });
        this.switch_button_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    prf.setString("notifications","true");
                }else{
                    prf.setString("notifications","false");
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
