package com.ristana.e_news.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ristana.e_news.R;
import com.ristana.e_news.adapter.ArticleAdapter;
import com.ristana.e_news.api.news.apiNews;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.entity.news.Article;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {


    private View view;
    private RecyclerView recycle_view_home_fragment;
    private SwipeRefreshLayout swipe_refreshl_home_fragment;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayoutManager;
    private Integer next_article_id=-1;
    private List<Article> articleList=new ArrayList<>();
    private ArticleAdapter articlesAdapter;
    private CardView card_refreshl_home_fragment_next;
    private CardView card_view_weather;
    private boolean doubleBackToExitPressedOnce;
    private RelativeLayout relative_layout_home_f;

    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view= inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        getArticle();
        initAction();
        return view;
    }

    private void getArticle() {
        this.swipe_refreshl_home_fragment.setRefreshing(true);
        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<List<Article>> call = service.articlesAll();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if(response.isSuccessful()){
                    articleList.clear();
                    for (int i=0;i<response.body().size();i++){
                        articleList.add(response.body().get(i));
                    }
                    articlesAdapter.notifyDataSetChanged();
                    if (response.body().size()!=0){
                        next_article_id=response.body().get(articleList.size()-1).getId();
                        loading = true;
                    }
                }
                swipe_refreshl_home_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                swipe_refreshl_home_fragment.setRefreshing(false);
                Snackbar snackbar = Snackbar
                        .make(relative_layout_home_f, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
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
    public void initAction(){
        this.swipe_refreshl_home_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticle();
            }
        });
    }
    public void initView(){
        this.relative_layout_home_f=(RelativeLayout) this.view.findViewById(R.id.relative_layout_home_f);
        this.card_view_weather=(CardView)getActivity().findViewById(R.id.card_view_weather);
        this.linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        this.card_refreshl_home_fragment_next=(CardView) this.view.findViewById(R.id.card_refreshl_home_fragment_next);
        this.recycle_view_home_fragment=(RecyclerView) this.view.findViewById(R.id.recycle_view_home_fragment);
        this.swipe_refreshl_home_fragment=(SwipeRefreshLayout)  this.view.findViewById(R.id.swipe_refreshl_home_fragment);
        this.recycle_view_home_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = linearLayoutManager.getChildCount();
                    totalItemCount      = linearLayoutManager.getItemCount();
                    pastVisiblesItems   = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            getNextArticles();
                        }
                    }
                }else{

                }
            }
        });
        articlesAdapter=new ArticleAdapter(articleList,getActivity().getApplicationContext(),true);
        recycle_view_home_fragment.setHasFixedSize(true);
        recycle_view_home_fragment.setAdapter(articlesAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view_home_fragment.setLayoutManager(linearLayoutManager);
    }

    private void getNextArticles() {
        this.card_refreshl_home_fragment_next.setVisibility(View.VISIBLE);
        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<List<Article>> call = service.articlesNext(this.next_article_id);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        articleList.add(response.body().get(i));
                    }
                    articlesAdapter.notifyDataSetChanged();
                    if (response.body().size()!=0){
                        next_article_id=articleList.get(articleList.size()-1).getId();
                        loading = true;

                    }
                }

                card_refreshl_home_fragment_next.setVisibility(View.GONE);

            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

                card_refreshl_home_fragment_next.setVisibility(View.GONE);

                swipe_refreshl_home_fragment.setRefreshing(false);
                Snackbar snackbar = Snackbar
                        .make(relative_layout_home_f, getResources().getString(R.string.no_connexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getNextArticles();
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


}
