package com.example.scrollview.model;

import com.example.scrollview.HomeFragment;
import com.example.scrollview.R;

import java.util.ArrayList;
import java.util.List;

public class events {
    private static List<event> categories = generateItems();

    public static List<event> getCategories() {
        return categories;
    }

    private static List<event> generateItems() {
        List<event> items = new ArrayList<>();
        for (int i = 0; i < HomeFragment.mImageUrls.size(); i++) {
            items.add(new event("title:" + i,HomeFragment.mImageUrls.get(i)));
        }
        return items;
    }

    public static class event {
        public final String title;
        public final String imageUrl;


        private event(String title, String imageUrl) {
            this.title = title;
            this.imageUrl = imageUrl;
        }
    }

    private static String getImageUrl(int position) {
        return categories.get(position).imageUrl;

    }
}
