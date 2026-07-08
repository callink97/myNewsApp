package com.example.newsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.models.NewsArticle;

import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NewsArticle article, int position);
    }

    private List<NewsArticle> articleList;
    private final List<NewsArticle> originalList;
    private final OnItemClickListener listener;

    public NewsAdapter(List<NewsArticle> articleList, OnItemClickListener listener) {
        this.articleList  = new ArrayList<>(articleList);
        this.originalList = new ArrayList<>(articleList);
        this.listener     = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articleList.get(position);
        holder.bind(article, listener, position);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            articleList = new ArrayList<>(originalList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            List<NewsArticle> filtered = new ArrayList<>();
            for (NewsArticle article : originalList) {
                if ((article.getTitle() != null &&
                        article.getTitle().toLowerCase().contains(lowerQuery)) ||
                    (article.getDescription() != null &&
                        article.getDescription().toLowerCase().contains(lowerQuery))) {
                    filtered.add(article);
                }
            }
            articleList = filtered;
        }
        notifyDataSetChanged();
    }

    public NewsArticle getArticleAt(int position) {
        return articleList.get(position);
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvSource;
        private final TextView tvDate;
        private final TextView tvDescription;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle       = itemView.findViewById(R.id.tvTitle);
            tvSource      = itemView.findViewById(R.id.tvSource);
            tvDate        = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(NewsArticle article, OnItemClickListener listener, int position) {
            tvTitle.setText(article.getTitle());
            tvSource.setText(article.getSourceName());
            tvDescription.setText(article.getDescription());

             String date = article.getPublishedAt();
            if (date != null && date.contains("T")) {
                date = date.substring(0, date.indexOf("T"));
            }
            tvDate.setText(date);

             itemView.setOnClickListener(v -> listener.onItemClick(article, position));
        }
    }
}
