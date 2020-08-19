package com.example.scrollview.model;

import com.example.scrollview.HomeFragment;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class events {
    private static List<event> categories = generateItems();



    public static List<event> getCategories() {
        return categories;
    }

    private static List<event> generateItems() {
        List<event> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add(new event());
        }
        return items;
    }
    public List<event> geteventlist()
    {
        return categories;
    }


    public static class event {
        public final String title;
        public final String imageUrl;
        public final Timestamp date_time;
        public final String description;
        public final String posterUrl;
        public final String venu;


        private event(String title, String imageUrl, Timestamp date_time, String description, String posterUrl, String venu) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.date_time = date_time;
            this.description = description;
            this.posterUrl = posterUrl;
            this.venu = venu;
        }
        public event()
        {
            this.date_time = new Timestamp(new Date());
            this.imageUrl = "https://mdg.iitr.ac.in/static/media/mdg_logo.fa8422e2.svg";
            this.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et " +
                                "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
                                "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla " +
                                 "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
            this.posterUrl = "https://cdn2.vectorstock.com/i/1000x1000/14/21/tailor-background-or-poster-with-dummy-vector-15521421.jpg";
            this.title = "MDG Talk";
            this.venu = "MAC Audi";

        }

    }

    private static String getImageUrl(int position) {
        return categories.get(position).imageUrl;

    }
}
