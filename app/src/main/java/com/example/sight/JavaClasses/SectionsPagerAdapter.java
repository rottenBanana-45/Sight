package com.example.sight.JavaClasses;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sight.Fragments.BookmarksFragment;
import com.example.sight.Fragments.FeedFragment;
import com.example.sight.Fragments.MyPostsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"FEED", "MY POSTS" , "BOOKMARKS"};


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                MyPostsFragment myPostsFragment = new MyPostsFragment();
                return myPostsFragment;

            case 0:
                FeedFragment feedFragment = new FeedFragment();
                return feedFragment;

            case 2:
                BookmarksFragment bookmarksFragment = new BookmarksFragment();
                return bookmarksFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
