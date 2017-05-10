package com.ristana.e_news.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ristana.e_news.R;
import com.ristana.e_news.adapter.ArticleAdapter;
import com.ristana.e_news.adapter.CommentAdapter;
import com.ristana.e_news.api.news.apiNews;
import com.ristana.e_news.api.news.newsClient;
import com.ristana.e_news.entity.news.ApiResponse;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.Category;
import com.ristana.e_news.entity.news.Comment;
import com.ristana.e_news.entity.news.ORMArticle;
import com.ristana.e_news.manager.PrefManager;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArticleActivity extends AppCompatActivity {

    private TextView text_view_article_time;
    private WebView web_view_article_content;
    private TextView text_view_article_title;
    private ImageView image_view_article_image;
    private FloatingActionButton fab;
    private String title_article;
    private String image_article;
    private String content_article;
    private String id_article;
    private String created_article;
    private Toolbar myToolbar;
    private ImageButton image_button_add_comment;
    private EditText edit_text_comment;
    private RelativeLayout relativeLayout;
    private RecyclerView recycle_view_article;
    private ProgressBar progress_bar_article;
    private LinearLayoutManager linearLayoutManager;
    private List<Comment> commentList =new ArrayList<>();
    private CommentAdapter commentAdapter;
    private RelativeLayout relativeLayout_write_comment;
    private String comment_article;
    private String category_article;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initView();
        initAction();
        Intent intent = getIntent();
        showAdsBanner();
        processIntent(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_save:
                List<ORMArticle> guideORMList = ORMArticle.find(ORMArticle.class, "num = ? ", id_article);
                if (guideORMList.size() == 0) {
                    ORMArticle ormArticle= new ORMArticle();
                    ormArticle.setNum(Long.valueOf(id_article));
                    ormArticle.setTitle(title_article);
                    if (comment_article.equals("false")){
                        ormArticle.setComment(false);
                    }else{
                        ormArticle.setComment(true);
                    }
                    ormArticle.setCategory(category_article);
                    ormArticle.setImage(image_article);
                    ormArticle.setCategory(category_article);
                    ormArticle.setCreated(created_article);
                    ormArticle.setVideo("");
                    ormArticle.setContent(content_article);
                    ormArticle.setType("article");
                    ormArticle.save();
                    item.setIcon(R.drawable.ic_bookmark);
                }else{

                    ORMArticle book = ORMArticle.findById(ORMArticle.class,guideORMList.get(0).getId());
                    if (book!=null){
                        book.delete();
                        item.setIcon(R.drawable.ic_marker);
                    }
                }
                break;
            case R.id.action_share:
                String shareBody = title_article +" \n\n "+ android.text.Html.fromHtml(content_article).toString().replace("img{max-width:100% !important}","") +" \n\n I Would like to share this with you. Here You Can Download "+getString(R.string.app_name)+" Application from   "+getString(R.string.url_app_google_play);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        List<ORMArticle> guideORMList = ORMArticle.find(ORMArticle.class, "num = ? ", id_article);
        if (guideORMList.size() == 0) {
            menu.getItem(0).setIcon(R.drawable.ic_marker);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_bookmark);
        }
        return true;
    }
    private  void initAction(){
        this.image_button_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

    }
    private void initView(){
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.image_button_add_comment=(ImageButton) findViewById(R.id.image_button_add_comment);
        this.edit_text_comment=(EditText) findViewById(R.id.edit_text_comment);
        this.web_view_article_content=(WebView) findViewById(R.id.web_view_article_content);
        this.text_view_article_title=(TextView) findViewById(R.id.text_view_article_title);
        this.text_view_article_time=(TextView) findViewById(R.id.text_view_article_time);
        this.image_view_article_image=(ImageView) findViewById(R.id.image_view_article_image);
        this.relativeLayout=(RelativeLayout) findViewById(R.id.relativeLayout);
        this.recycle_view_article=(RecyclerView) findViewById(R.id.recycle_view_article);
        this.progress_bar_article=(ProgressBar) findViewById(R.id.progress_bar_article);
        this.relativeLayout_write_comment=(RelativeLayout) findViewById(R.id.relativeLayout_write_comment);
        this.commentAdapter = new CommentAdapter(commentList,getApplicationContext());


        web_view_article_content.setWebChromeClient(new WebChromeClient());
        web_view_article_content.setWebViewClient(new WebViewClient());
        web_view_article_content.getSettings().setJavaScriptEnabled(true);
        web_view_article_content.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://")  || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });

        recycle_view_article.setHasFixedSize(true);
        recycle_view_article.setAdapter(commentAdapter);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view_article.setLayoutManager(linearLayoutManager);
    }
    public void addComment(){
        PrefManager prf= new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")){
                Retrofit retrofit = newsClient.getClient();
                apiNews service = retrofit.create(apiNews.class);
                Call<ApiResponse> call = service.addComment(prf.getString("ID_USER").toString(),id_article,edit_text_comment.getText().toString());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().getCode()==200){
                                Toast.makeText(ArticleActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                edit_text_comment.setText("");
                                String content="";
                                String id="";
                                String author="";

                                for (int i=0;i<response.body().getValues().size();i++){
                                    if (response.body().getValues().get(i).getName().equals("id")){
                                        id=response.body().getValues().get(i).getValue();
                                    }
                                    if (response.body().getValues().get(i).getName().equals("content")){
                                        content=response.body().getValues().get(i).getValue();
                                    }
                                    if (response.body().getValues().get(i).getName().equals("author")){
                                        author=response.body().getValues().get(i).getValue();
                                    }

                                }
                                Comment comment= new Comment();
                                comment.setAuthor(author);
                                comment.setContent(content);
                                comment.setEnabled(true);
                                comment.setCreated("now");
                                commentList.add(comment);
                                commentAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addComment();
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                });
        }else{
            Intent intent = new Intent(ArticleActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }
    public void getComment(){
        progress_bar_article.setVisibility(View.VISIBLE);
        Retrofit retrofit = newsClient.getClient();
        apiNews service = retrofit.create(apiNews.class);
        Call<List<Comment>> call = service.getComments(id_article);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()){
                   for (int i=0;i<response.body().size();i++){
                       commentList.add(response.body().get(i));
                   }
                    commentAdapter.notifyDataSetChanged();
                }
                progress_bar_article.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getComment();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
                progress_bar_article.setVisibility(View.GONE
                );
            }
        });
    }
    public void setContent(String content){
        if (content != null){
            content_article ="<style type=\"text/css\">img{max-width:100%  !important}</style>"+ content;

            web_view_article_content.loadData(content_article, "text/html; charset=utf-8", "utf-8");
        }else{
            Retrofit retrofit = newsClient.getClient();
            apiNews service = retrofit.create(apiNews.class);
            Call<Article> call = service.getArticle(id_article);
            call.enqueue(new Callback<Article>() {
                @Override
                public void onResponse(Call<Article> call, Response<Article> response) {
                    if (response.isSuccessful()){
                        content_article ="<style type=\"text/css\">img{max-width:100% !important}</style>"+ response.body().getContent();
                        web_view_article_content.loadData(content_article, "text/html; charset=utf-8", "utf-8");
                    }
                }
                @Override
                public void onFailure(Call<Article> call, Throwable t) {

                }
            });

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        commentList.clear();
        commentAdapter.notifyDataSetChanged();
        id_article = intent.getStringExtra("id_article");
        title_article = intent.getStringExtra("title_article");
        created_article = intent.getStringExtra("created_article");
        image_article = intent.getStringExtra("image_article");
        category_article = intent.getStringExtra("category_article");
        setContent( intent.getStringExtra("content_article"));
        text_view_article_title.setText(title_article);
        text_view_article_time.setText(created_article);
        Picasso.with(getApplicationContext()).load(image_article).placeholder(R.drawable.image6409).into(image_view_article_image);

        comment_article = intent.getStringExtra("comment_article");

        if (comment_article.equals("false")){
            relativeLayout_write_comment.setVisibility(View.GONE);
        }else{
            relativeLayout_write_comment.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setTitle(title_article);
        getComment();
    }
    private void processIntent(Intent intent){

        id_article = intent.getStringExtra("id_article");
        title_article = intent.getStringExtra("title_article");
        created_article = intent.getStringExtra("created_article");
        image_article = intent.getStringExtra("image_article");
        category_article = intent.getStringExtra("category_article");
        comment_article = intent.getStringExtra("comment_article");

        if (comment_article.equals("false")){
            relativeLayout_write_comment.setVisibility(View.GONE);
        }else{
            relativeLayout_write_comment.setVisibility(View.VISIBLE);
        }

        setContent( intent.getStringExtra("content_article"));
        text_view_article_title.setText(title_article);
        text_view_article_time.setText(created_article);
        Picasso.with(getApplicationContext()).load(image_article).placeholder(R.drawable.image6409).into(image_view_article_image);

        getSupportActionBar().setTitle(title_article);
        getComment();
    }
    @Override
    public void  onResume(){
        super.onResume();

        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","true");
    }
    @Override
    public void onStart(){
        super.onStart();
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","true");
    }
    @Override
    public void onPause(){
        super.onPause();
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.setString("APP_RUN","false");
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
    private void showAdsBanner() {

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        if (getString(R.string.AD_MOB_ENABLED_BANNER).equals("false")){
            mAdView.setVisibility(View.GONE);
        }

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

    }

}
