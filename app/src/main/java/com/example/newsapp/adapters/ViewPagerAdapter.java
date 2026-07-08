package com.example.newsapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.newsapp.fragments.NewsDetailFragment;
import com.example.newsapp.fragments.NewsListFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
         if (position == 0) return new NewsListFragment();
        return new NewsDetailFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs
    }
}
