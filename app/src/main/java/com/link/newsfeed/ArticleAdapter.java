package com.link.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.newsfeed.model.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Article> mArticles = new ArrayList<>();

    private List<Article> ArticlesList = null;
    private List<Article> filteredArticlesList;
    private ArticleFilter articleFilter;


    ArticleAdapter(Context context, ArrayList<Article> articles) {
        mContext = context;
        mArticles = articles;
        this.ArticlesList = articles;
        this.filteredArticlesList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_row, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article currentArticle = mArticles.get(position);

        Picasso.with(mContext).load(currentArticle.getUrlToImage()).into(holder.ArticleImage);
        holder.ArticleTitle.setText(currentArticle.getTitle());
        String Author = "By " + currentArticle.getAuthor();
        holder.ArticleAuthor.setText(Author);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
        try {
            Date date = dateFormat.parse(currentArticle.getPublishedAt());
            dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            holder.ArticlePublishedAt.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public Filter getFilter() {
        if (articleFilter == null)
            articleFilter = new ArticleFilter(this, ArticlesList);
        return articleFilter;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ArticleImage;
        TextView ArticleTitle, ArticleAuthor, ArticlePublishedAt;
        CardView ArticleCard;

        ViewHolder(View itemView) {
            super(itemView);
            ArticleImage = itemView.findViewById(R.id.article_image);
            ArticleTitle = itemView.findViewById(R.id.article_title);
            ArticleAuthor = itemView.findViewById(R.id.article_author);
            ArticlePublishedAt = itemView.findViewById(R.id.article_publishedAt);
            ArticleCard = itemView.findViewById(R.id.article_card);

            ArticleCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.article_card:
                    Intent ArticleDetailsIntent = new Intent(mContext, ArticleDetails.class);
                    Bundle extras = new Bundle();
                    extras.putParcelable("Article", mArticles.get(getAdapterPosition()));
                    ArticleDetailsIntent.putExtras(extras);
                    mContext.startActivity(ArticleDetailsIntent);
                    break;
            }
        }
    }


    private static class ArticleFilter extends Filter {

        private final ArticleAdapter adapter;

        private List<Article> originalList = new ArrayList<>();

        private final List<Article> filteredList;

        private ArticleFilter(ArticleAdapter adapter, List<Article> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Article article : originalList) {
                    if (article.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(article);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredArticlesList.clear();
            adapter.filteredArticlesList.addAll((ArrayList<Article>) results.values);
            adapter.mArticles.clear();
            adapter.mArticles.addAll(filteredList);
            adapter.notifyDataSetChanged();
        }
    }
}
