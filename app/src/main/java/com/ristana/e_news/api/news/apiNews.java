package com.ristana.e_news.api.news;

import com.ristana.e_news.config.Config;
import com.ristana.e_news.entity.news.ApiResponse;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.Category;
import com.ristana.e_news.entity.news.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hsn on 21/02/2017.
 */

public interface apiNews {
    @FormUrlEncoded
    @POST("user/register/"+ Config.TOKEN_APP+"/")
    Call<ApiResponse> register(@Field("name") String name, @Field("username") String username, @Field("password") String password,@Field("type") String type);

    @GET("user/login/{email}/{password}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> login(@Path("email") String email, @Path("password") String password);

    @GET("user/password/{id}/{old}/{new_}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> changePassword(@Path("id") String id,@Path("old") String old,@Path("new_") String new_);


    @GET("user/reset/{id}/{key}/{new_}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> reset(@Path("id") String id,@Path("key") String key,@Path("new_") String new_);

    @GET("user/check/{id}/{key}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> check(@Path("id") String id,@Path("key") String key);


    @GET("user/name/{id}/{name}/{key}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> editName(@Path("id") String id,@Path("name") String name,@Path("key") String key);

    @GET("user/email/{email}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> sendEmail(@Path("email") String email);


    @GET("user/request/{key}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> request(@Path("key") String key);


    @GET("categories/list/"+Config.TOKEN_APP+"/")
    Call<List<Category>> categorriesList();

    @GET("articles/all/"+Config.TOKEN_APP+"/")
    Call<List<Article>> articlesAll();

    @GET("articles/next/{id}/"+Config.TOKEN_APP+"/")
    Call<List<Article>> articlesNext(@Path("id")  Integer id);


    @GET("articles/by/{category}/"+Config.TOKEN_APP+"/")
    Call<List<Article>> articlesByCategory(@Path("category")  String category);

    @GET("articles/search/{query}/"+Config.TOKEN_APP+"/")
    Call<List<Article>> articlesByQuery(@Path("query")  String query);



    @GET("articles/next/{category}/{id}/"+Config.TOKEN_APP+"/")
    Call<List<Article>> articlesByCategoryNext(@Path("category")  String category,@Path("id") Integer id);


    @GET("articles/get/{id}/"+Config.TOKEN_APP+"/")
    Call<Article> getArticle(@Path("id")  String id);


    @GET("comments/add/{user}/{article}/{comment}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> addComment(@Path("user")  String user,@Path("article") String article,@Path("comment") String comment);

    @GET("comments/by/{article}/"+Config.TOKEN_APP+"/")
    Call<List<Comment>> getComments(@Path("article")  String article);

    @GET("device/{tkn}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> addDevice(@Path("tkn")  String tkn);

    @GET("support/add/{email}/{name}/{message}/"+Config.TOKEN_APP+"/")
    Call<ApiResponse> addSupport(@Path("email") String email, @Path("name") String name , @Path("message") String message);

}
