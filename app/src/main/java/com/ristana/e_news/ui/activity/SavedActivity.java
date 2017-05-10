package com.ristana.e_news.ui.activity;

import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ristana.e_news.R;
import com.ristana.e_news.adapter.ArticleAdapter;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.ORMArticle;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {
    private List<Article> articleList=new ArrayList<>();
    private RelativeLayout activity_saved;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recycle_view_saved;
    private SwipeRefreshLayout swipe_refreshl_saved;
    private ArticleAdapter articlesAdapter;
    private Toolbar myToolbar;
    private ImageView imageView_empty_saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
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

        swipe_refreshl_saved.setRefreshing(true);
        List<ORMArticle> guideORMList = ORMArticle.listAll(ORMArticle.class,"id DESC");
        if (guideORMList.size()!=0){
            articleList.clear();
            for (int i=0;i<guideORMList.size();i++){
                Article a= new Article();
                a.setORM(guideORMList.get(i));
                articleList.add(a);
            }
            articlesAdapter.notifyDataSetChanged();
            imageView_empty_saved.setVisibility(View.GONE);
            recycle_view_saved.setVisibility(View.VISIBLE);
        }else{
            imageView_empty_saved.setVisibility(View.VISIBLE);
            recycle_view_saved.setVisibility(View.GONE);
        }

        swipe_refreshl_saved.setRefreshing(false);

    }
    public void initView(){

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.action_saved));
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
