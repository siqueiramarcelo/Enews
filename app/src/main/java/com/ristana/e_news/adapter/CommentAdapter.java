package com.ristana.e_news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.ristana.e_news.R;
import com.ristana.e_news.entity.news.Article;
import com.ristana.e_news.entity.news.Comment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hsn on 01/03/2017.
 */

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new CommentAdapter.CommentHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.text_view_comment_time.setText(commentList.get(position).getCreated());
       if (!commentList.get(position).getEnabled()){
           holder.text_view_comment_content.setText("this comment has been hidden");
           holder.text_view_comment_content.setTextColor(context.getResources().getColor(R.color.graycolor));
       }else{
           holder.text_view_comment_content.setText(commentList.get(position).getContent());
       }

        holder.text_view_comment_name.setText(commentList.get(position).getAuthor());


        int color=Math.abs(commentList.get(position).getAuthor().hashCode()) % 9;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(commentList.get(position).getAuthor().substring(0,1).toUpperCase(), context.getResources().getColor(context.getResources().getIdentifier("contact_"+color, "color", context.getPackageName())));
        holder.image_view_comment.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        private ImageView image_view_comment;
        private TextView text_view_comment_content;
        private TextView text_view_comment_time;
        private TextView text_view_comment_name;
        public CommentHolder(View itemView) {
            super(itemView);
            this.image_view_comment=(ImageView) itemView.findViewById(R.id.image_view_comment);
            this.text_view_comment_content=(TextView) itemView.findViewById(R.id.text_view_comment_content);
            this.text_view_comment_name=(TextView) itemView.findViewById(R.id.text_view_comment_name);
            this.text_view_comment_time=(TextView) itemView.findViewById(R.id.text_view_comment_time);
        }
    }
}
