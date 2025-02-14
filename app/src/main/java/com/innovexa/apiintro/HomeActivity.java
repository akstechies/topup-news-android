package com.innovexa.apiintro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HomeActivity extends AppCompatActivity {
    ArrayList<UserModel> allNewsList;
    private RequestQueue requestQueue;
    RecyclerView recView;
    ProgressBar progressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progressBar);
        recView = findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        allNewsList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Initial API call
        fetchNewsData();

        // Swipe down to refresh
        // Refresh news
        swipeRefreshLayout.setOnRefreshListener(this::fetchNewsData);
    }


    private void fetchNewsData() {
        String url = "https://newsdata.io/api/1/news?apikey=pub_28085bda5f01137cbe172a9cc9d64ab4994ba&q=india";

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        recView.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray articlesArray = response.getJSONArray("results");

                        allNewsList.clear(); // Clear previous data
                        List<UserModel> updatedNewsList = new ArrayList<>();

                        for (int i = 0; i < articlesArray.length(); i++) {
                            JSONObject articleObject = articlesArray.getJSONObject(i);
                            updatedNewsList.add(new UserModel(
                                    articleObject.optString("title", ""),
                                    articleObject.optString("link", ""),
                                    articleObject.optString("description", ""),
                                    articleObject.optString("content", ""),
                                    articleObject.optString("image_url", ""),
                                    articleObject.optString("language", ""),
                                    articleObject.optString("pubDate", "")
                            ));
                        }

                        // Update adapter
                        allNewsList.addAll(updatedNewsList);
                        recView.setAdapter(new NewsAdapter(getApplicationContext(), allNewsList));

                        // Hide progress bar
                        progressBar.setVisibility(View.GONE);
                        recView.setVisibility(View.VISIBLE);

                        // Stop SwipeRefreshLayout loading
                        swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("myApp", "JSON parsing error: " + e.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                error -> {
                    Log.e("myApp", "API request failed: " + error.toString());

                    // Hide progress bar and stop refresh animation
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

}
