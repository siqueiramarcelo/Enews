package com.ristana.e_news.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ristana.e_news.R;
import com.ristana.e_news.adapter.ArticleAdapter;
import com.ristana.e_news.api.news.apiNews;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.ORMArticle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private List<Article> articleList=new ArrayList<>();
    private RelativeLayout activity_saved;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recycle_view_saved;
    private SwipeRefreshLayout swipe_refreshl_saved;
    private ArticleAdapter articlesAdapter;
    private Toolbar myToolbar;
    private String query="";
    private ImageView imageView_empty_saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        query=intent.getStringExtra("query");

        setContentView(R.layout.activity_search);
        initView();
        initAction();
        getArticle();
    }


    public void initAction(){
        this.swipe_refreshl_saved.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticle();
            }
        });
    }


    private void getArticle() {
        this.swipe_refreshl_saved.setRefreshing(true);
        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<List<Article>> call = service.articlesByQuery(query);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()!=0){
                        articleList.clear();
                        for (int i=0;i<response.body().size();i++){
                            articleList.add(response.body().get(i));
                        }
                        articlesAdapter.notifyDataSetChanged();
                        articlesAdapter.notifyDataSetChanged();
                        imageView_empty_saved.setVisibility(View.GONE);
                        recycle_view_saved.setVisibility(View.VISIBLE);
                    }else{
                        imageView_empty_saved.setVisibility(View.VISIBLE);
                        recycle_view_saved.setVisibility(View.GONE);
                    }
                }
                swipe_refreshl_saved.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                swipe_refreshl_saved.setRefreshing(false);


                Snackbar snackbar = Snackbar
                        .make(activity_saved, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getArticle();
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
    public void initView(){

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(query);
        this.activity_saved=(RelativeLayout) findViewById(R.id.activity_saved);

        this.linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recycle_view_saved=(RecyclerView) findViewById(R.id.recycle_view_saved);
        this.swipe_refreshl_saved=(SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_saved);
        this.imageView_empty_saved=(ImageView) findViewById(R.id.imageView_empty_saved);


        articlesAdapter=new ArticleAdapter(articleList,getApplicationContext(),true,true);
        recycle_view_saved.setHasFixedSize(true);
        recycle_view_saved.setAdapter(articlesAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view_saved.setLayoutManager(linearLayoutManager);
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
