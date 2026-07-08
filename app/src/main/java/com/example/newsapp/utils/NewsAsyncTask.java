package com.example.newsapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.newsapp.models.NewsArticle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NewsAsyncTask extends AsyncTask<String, Void, List<NewsArticle>> {

    private static final String TAG = "NewsAsyncTask";

     public interface NewsCallback {
        void onSuccess(List<NewsArticle> articles);
        void onError(String errorMessage);
    }

    private final NewsCallback callback;
    private String errorMessage = null;

    public NewsAsyncTask(NewsCallback callback) {
        this.callback = callback;
    }


    @Override
    protected List<NewsArticle> doInBackground(String... params) {
        String apiUrl = params[0];
        List<NewsArticle> articles = new ArrayList<>();

        HttpURLConnection connection = null;
        BufferedReader reader       = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 10 seconds
            connection.setReadTimeout(10000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                errorMessage = "Server error: " + responseCode;
                return null;
            }

             reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

             JSONObject jsonObject = new JSONObject(result.toString());
            String status = jsonObject.optString("status", "");

            if (!status.equals("ok")) {
                errorMessage = jsonObject.optString("message", "Unknown API error");
                return null;
            }

            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleObj = articlesArray.getJSONObject(i);

                 JSONObject sourceObj = articleObj.optJSONObject("source");
                String sourceName = (sourceObj != null)
                        ? sourceObj.optString("name", "Unknown") : "Unknown";

                String title       = articleObj.optString("title",       "No Title");
                String description = articleObj.optString("description", "No description available.");
                String content     = articleObj.optString("content",     "No content available.");
                String author      = articleObj.optString("author",      "Unknown Author");
                String publishedAt = articleObj.optString("publishedAt", "");
                String urlToImage  = articleObj.optString("urlToImage",  "");
                String articleUrl  = articleObj.optString("url",         "");

                 if (title.equals("[Removed]")) continue;

                articles.add(new NewsArticle(
                        title, description, content,
                        author, publishedAt,
                        urlToImage, articleUrl, sourceName));
            }

        } catch (Exception e) {
            Log.e(TAG, "Error fetching news", e);
            errorMessage = e.getMessage();
            return null;
        } finally {
            if (connection != null) connection.disconnect();
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {}
        }

        return articles;
    }

     @Override
    protected void onPostExecute(List<NewsArticle> articles) {
        if (articles == null || errorMessage != null) {
            callback.onError(errorMessage != null ? errorMessage : "Failed to load news.");
        } else if (articles.isEmpty()) {
            callback.onError("No articles found.");
        } else {
            callback.onSuccess(articles);
        }
    }
}
