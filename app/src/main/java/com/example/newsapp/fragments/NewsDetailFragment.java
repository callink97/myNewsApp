package com.example.newsapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.models.NewsArticle;

public class NewsDetailFragment extends Fragment {

    private TextView tvDetailTitle;
    private TextView tvDetailSource;
    private TextView tvDetailDate;
    private TextView tvDetailAuthor;
    private TextView tvDetailDescription;
    private TextView tvDetailContent;
    private Button   btnReadMore;

    private String articleUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        // Bind views
        tvDetailTitle       = view.findViewById(R.id.tvDetailTitle);
        tvDetailSource      = view.findViewById(R.id.tvDetailSource);
        tvDetailDate        = view.findViewById(R.id.tvDetailDate);
        tvDetailAuthor      = view.findViewById(R.id.tvDetailAuthor);
        tvDetailDescription = view.findViewById(R.id.tvDetailDescription);
        tvDetailContent     = view.findViewById(R.id.tvDetailContent);
        btnReadMore         = view.findViewById(R.id.btnReadMore);

        // Open full article in browser
        btnReadMore.setOnClickListener(v -> {
            if (!articleUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                startActivity(intent);
            }
        });

         if (getActivity() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            fillViews(
                    extras.getString("title",       ""),
                    extras.getString("sourceName",  ""),
                    extras.getString("publishedAt", ""),
                    extras.getString("author",      ""),
                    extras.getString("description", ""),
                    extras.getString("content",     ""),
                    extras.getString("url",         "")
            );
        }

        return view;
    }

    public void displayArticle(NewsArticle article) {
        if (article == null) return;
        fillViews(
                article.getTitle(),
                article.getSourceName(),
                article.getPublishedAt(),
                article.getAuthor(),
                article.getDescription(),
                article.getContent(),
                article.getUrl()
        );
    }

     private void fillViews(String title, String source, String date,
                           String author, String description,
                           String content, String url) {
        if (tvDetailTitle == null) return;

        tvDetailTitle.setText(title.isEmpty() ? "No Title" : title);
        tvDetailSource.setText("Source: " + (source.isEmpty() ? "Unknown" : source));
        tvDetailAuthor.setText("Author: " + (author.isEmpty() ? "Unknown" : author));
        tvDetailDescription.setText(description.isEmpty() ? "No description." : description);

         String cleanContent = content;
        if (cleanContent != null && cleanContent.contains("[+")) {
            cleanContent = cleanContent.substring(0, cleanContent.lastIndexOf("[+")).trim();
        }
        tvDetailContent.setText(cleanContent == null || cleanContent.isEmpty()
                ? "No content available." : cleanContent);

         if (date != null && date.contains("T")) {
            date = date.substring(0, date.indexOf("T"));
        }
        tvDetailDate.setText("Published: " + (date == null || date.isEmpty() ? "Unknown" : date));

        this.articleUrl = (url == null) ? "" : url;
        btnReadMore.setVisibility(articleUrl.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
