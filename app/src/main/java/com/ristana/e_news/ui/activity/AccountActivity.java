package com.ristana.e_news.ui.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ristana.e_news.R;
import com.ristana.e_news.manager.PrefManager;

import static com.ristana.e_news.R.id.header_text_view_name;

public class AccountActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private PrefManager prf;
    private TextView header_text_view_name;
    private RelativeLayout relativeLayout_logout;
    private RelativeLayout relativeLayout_password;
    private RelativeLayout relativeLayout_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prf= new PrefManager(getApplicationContext());

        initView();
        initAction();
        if (prf.getString("LOGGED").toString().equals("TRUE")){
            header_text_view_name.setText(prf.getString("NAME_USER").toString());
        }

    }
    public void initView(){
        setContentView(R.layout.activity_account);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        this.header_text_view_name=(TextView) findViewById(R.id.header_text_view_name);
        this.relativeLayout_logout=(RelativeLayout) findViewById(R.id.relativeLayout_logout);
        this.relativeLayout_edit=(RelativeLayout) findViewById(R.id.relativeLayout_edit);
        this.relativeLayout_password=(RelativeLayout) findViewById(R.id.relativeLayout_password );
        if ( !prf.getString("TYPE_USER").equals("email")){
            this.relativeLayout_password.setVisibility(View.GONE);

        }else{
            this.relativeLayout_password.setVisibility(View.VISIBLE);
        }

    }
    public void initAction(){
        this.relativeLayout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        this.relativeLayout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this,PasswordActivity.class));
            }
        });
        this.relativeLayout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this,EditActivity.class));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

        }
        return super.onOptionsItemSelected(item);
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
        }else{
            finish();
        }
        Toast.makeText(getApplicationContext(),getString(R.string.message_logout),Toast.LENGTH_LONG).show();
    }
}
