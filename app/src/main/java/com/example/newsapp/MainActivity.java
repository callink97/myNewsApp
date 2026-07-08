package com.example.newsapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.newsapp.adapters.ViewPagerAdapter;
import com.example.newsapp.fragments.NewsListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

         viewPager.setOffscreenPageLimit(1);

         TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("📰 News");
            } else {
                tab.setText("📄 Details");
            }
        }).attach();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
             viewPager.setCurrentItem(0, true);
            refreshNewsList();
            Toast.makeText(this, "Refreshing news...", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_about) {
            Toast.makeText(this,
                    "News App — Built with NewsAPI\nDeveloper: Sally Aiman ",
                    Toast.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.menu_top_headlines) {
            viewPager.setCurrentItem(0, true);
            Toast.makeText(this, "Showing Top Headlines", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

     private void refreshNewsList() {
        NewsListFragment fragment = (NewsListFragment)
                getSupportFragmentManager().findFragmentByTag("f0");
        if (fragment != null) {
            fragment.refreshData();
        }
    }
}
