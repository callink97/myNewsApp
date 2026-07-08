package com.example.newsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.newsapp.R;
import com.example.newsapp.adapters.NewsAdapter;
import com.example.newsapp.models.NewsArticle;
import com.example.newsapp.utils.NewsAsyncTask;
import com.example.newsapp.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.List;


public class NewsListFragment extends Fragment
        implements NewsAsyncTask.NewsCallback, NewsAdapter.OnItemClickListener {

     private static final String API_KEY =
            "b76ab4ebd1ab4503b0f5ffe21f1f2aa5";
    private static final String API_URL =
            "https://newsapi.org/v2/top-headlines?country=us&pageSize=30&apiKey=" + API_KEY;

    private RecyclerView     recyclerView;
    private NewsAdapter      adapter;
    private ProgressBar      progressBar;
    private TextView         tvError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView       searchView;

    private List<NewsArticle> articleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

         recyclerView       = view.findViewById(R.id.recyclerView);
        progressBar        = view.findViewById(R.id.progressBar);
        tvError            = view.findViewById(R.id.tvError);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        searchView         = view.findViewById(R.id.searchView);

         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter(articleList, this);
        recyclerView.setAdapter(adapter);

         swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchNews();
        });

         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

         fetchNews();
        return view;
    }

     public void refreshData() {
        fetchNews();
    }

     private void fetchNews() {
        tvError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new NewsAsyncTask(this).execute(API_URL);
    }


    @Override
    public void onSuccess(List<NewsArticle> articles) {
        if (getActivity() == null) return;

        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);

        articleList.clear();
        articleList.addAll(articles);
        adapter = new NewsAdapter(articleList, this);
        recyclerView.setAdapter(adapter);

         NotificationHelper.showNotification(
                getContext(),
                "News Loaded ✅",
                articles.size() + " articles loaded successfully.");

         if (!articles.isEmpty()) {
            passArticleToDetail(articles.get(0));
        }
    }

    @Override
    public void onError(String errorMessage) {
        if (getActivity() == null) return;

        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText("⚠ " + errorMessage);

         NotificationHelper.showNotification(
                getContext(),
                "Error ❌",
                "Failed to load news: " + errorMessage);
    }


    @Override
    public void onItemClick(NewsArticle article, int position) {
        passArticleToDetail(article);

         if (getActivity() != null) {
            ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
            if (viewPager != null) viewPager.setCurrentItem(1, true);
        }
    }
     private void passArticleToDetail(NewsArticle article) {
        if (getActivity() == null) return;

         NewsDetailFragment detailFragment = (NewsDetailFragment)
                getActivity().getSupportFragmentManager()
                        .findFragmentByTag("f1");

        if (detailFragment != null) {
            detailFragment.displayArticle(article);
        } else {
             Bundle bundle = new Bundle();
            bundle.putString("title",       article.getTitle());
            bundle.putString("description", article.getDescription());
            bundle.putString("content",     article.getContent());
            bundle.putString("author",      article.getAuthor());
            bundle.putString("publishedAt", article.getPublishedAt());
            bundle.putString("urlToImage",  article.getUrlToImage());
            bundle.putString("url",         article.getUrl());
            bundle.putString("sourceName",  article.getSourceName());

            getActivity().getIntent().putExtras(bundle);
        }
    }
}
