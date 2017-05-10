package com.ristana.e_news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ristana.e_news.R;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.ORMArticle;
import com.ristana.e_news.ui.activity.ArticleActivity;
import com.ristana.e_news.ui.activity.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hsn on 24/02/2017.
 */

public class ArticleAdapter  extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder>{
    private List<Article> articleList;
    private Context context;
    private Boolean shwocategorie = true;
    private Boolean savedList =  false;
    public ArticleAdapter(List<Article> articleList, Context context, Boolean shwocategorie) {
        this.articleList = articleList;
        this.context = context;
        this.shwocategorie = shwocategorie;
    }
    public ArticleAdapter(List<Article> articleList, Context context, Boolean shwocategorie,Boolean savedList) {
        this.articleList = articleList;
        this.context = context;
        this.shwocategorie = shwocategorie;
        this.savedList=savedList;
    }
    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ArticleHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final ArticleHolder holder,final int position) {
        //holder.text_View_title_article_item.setText(articleList.get(position).getTitle());
        holder.text_view_time_article_item.setText(articleList.get(position).getCreated());
        holder.text_view_content_text_view_time_article_item.setText(articleList.get(position).getTitle());
        Picasso.with(context).load(articleList.get(position).getImage()).placeholder(R.drawable.image6409).into(holder.image_view_article_item);

        if (this.shwocategorie==true){
            holder.text_view_categoryarticle_item.setText(articleList.get(position).getCategory());
        }else{
            holder.text_view_categoryarticle_item.setVisibility(View.GONE);
        }
        if (articleList.get(position).getType().equals("article")){
            holder.imge_view_time_video.setVisibility(View.GONE);
        }else{
            holder.imge_view_time_video.setVisibility(View.VISIBLE);
        }
        List<ORMArticle> ormArticles = ORMArticle.find(ORMArticle.class, "num = ? ", articleList.get(position).getId()+"");
        if (ormArticles.size() == 0) {
            holder.image_view_save_article_item.setImageResource(R.drawable.ic_marker);
        } else {
            holder.image_view_save_article_item.setImageResource(R.drawable.ic_bookmark);
        }
        holder.image_view_save_article_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ORMArticle> guideORMList = ORMArticle.find(ORMArticle.class, "num = ? ", articleList.get(position).getId()+"");
                if (guideORMList.size() == 0) {
                    ORMArticle ormArticle= new ORMArticle(articleList.get(position));
                    ormArticle.save();
                    holder.image_view_save_article_item.setImageResource(R.drawable.ic_bookmark);
                }else{

                    ORMArticle book = ORMArticle.findById(ORMArticle.class,guideORMList.get(0).getId());
                   if (book!=null){
                       book.delete();
                       holder.image_view_save_article_item.setImageResource(R.drawable.ic_marker);
                        if (savedList==true){
                            holder.card_view_article_item.setVisibility(View.GONE);
                        }
                   }

                }


            }
        });
        holder.card_view_article_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (articleList.get(position).getType().equals("article")) {
                     intent = new Intent(context, ArticleActivity.class);

                }else{
                     intent = new Intent(context, VideoActivity.class);
                     intent.putExtra("video_article",articleList.get(position).getVideo());
                }
                intent.putExtra("id_article",articleList.get(position).getId()+"");
                intent.putExtra("title_article",articleList.get(position).getTitle());
                intent.putExtra("image_article",articleList.get(position).getImage());
                intent.putExtra("created_article",articleList.get(position).getCreated());
                intent.putExtra("content_article",articleList.get(position).getContent());
                intent.putExtra("category_article",articleList.get(position).getCategory());

                if (articleList.get(position).getComment()==true){
                    intent.putExtra("comment_article","true");
                }else{
                    intent.putExtra("comment_article","false");
                }

                holder.card_view_article_item.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleHolder extends RecyclerView.ViewHolder {
        private CardView    card_view_article_item;
        private ImageView   image_view_article_item;
        private TextView    text_View_title_article_item;
        private TextView    text_view_time_article_item;
        private TextView    text_view_content_text_view_time_article_item;
        private ImageView   image_view_save_article_item;
        private TextView    text_view_categoryarticle_item;
        private ImageView   imge_view_time_video;

        public ArticleHolder(View itemView) {
            super(itemView);
            this.card_view_article_item=(CardView) itemView.findViewById(R.id.card_view_article_item);
            this.image_view_article_item=(ImageView) itemView.findViewById(R.id.image_view_article_item);
            this.text_View_title_article_item=(TextView) itemView.findViewById(R.id.text_View_title_article_item);
            this.text_view_time_article_item=(TextView) itemView.findViewById(R.id.text_view_time_article_item);
            this.text_view_content_text_view_time_article_item=(TextView) itemView.findViewById(R.id.text_view_content_text_view_time_article_item);
            this.image_view_save_article_item= (ImageView) itemView.findViewById(R.id.image_view_save_article_item);
            this.text_view_categoryarticle_item=(TextView) itemView.findViewById(R.id.text_view_categoryarticle_item);
            this.imge_view_time_video=(ImageView) itemView.findViewById(R.id.imge_view_time_video);
        }

    }

}
