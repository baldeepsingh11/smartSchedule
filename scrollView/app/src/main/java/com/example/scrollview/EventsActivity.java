package com.example.scrollview;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.example.scrollview.model.events;
import com.google.gson.Gson;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

import static com.example.scrollview.R.id.action_home;
import static com.example.scrollview.R.id.image;
import static com.example.scrollview.model.events.getCategories;

public class EventsActivity extends AppCompatActivity {

    private PageIndicator indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inside your activity (if you did not enable transitions in your theme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getDelegate().setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new IconAdapter(this, getSupportFragmentManager()));
        indicator = (PageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        pager.setCurrentItem(getIntent().getIntExtra("position", 0), false);
        supportPostponeEnterTransition();
        pager.post(new Runnable() {
            @Override
            public void run() {
                supportStartPostponedEnterTransition();
            }
        });

    }


    private class IconAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
        private final Context context;

        private IconAdapter(Context context, FragmentManager manager) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return DetailPage.newInstance(context,position);
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @Override
        public int getCount() {
            return events.getCategories().size();
        }
    }

    public static class DetailPage extends Fragment {
        ImageView posterview;
        TextView title;
        TextView description;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootview = inflater.inflate(R.layout.detail_page, container, false);
             posterview = (ImageView) rootview.findViewById(R.id.event_poster);
             title = (TextView) rootview.findViewById(R.id.event_title);
             description = (TextView) rootview.findViewById(R.id.event_desc);
             //imageView=(ImageView) rootview.findViewById(R.id.)
            String posterUrl=null;
            events.event event1=new events.event();
            if (getArguments() != null) {
                String jsonString;
                jsonString = getArguments().getString("events");
                Log.i("fucked1", "onCreateView: "+jsonString);
                Gson gson= new Gson();
                event1=gson.fromJson(jsonString,event1.getClass());
                Log.i("fucked2", "onCreateView: "+jsonString);
                title.setText(event1.getTitle());
                description.setText(event1.getDescription());


            }
            Glide.with(getContext())
                    .asBitmap()
                    .load(event1.getPosterUrl())
                    .into(posterview);
           /* Glide.with(getContext())
                    .asBitmap()
                    .load(event.getImageUrl())
                    .into(imageView)*/



            return rootview;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }

        public static DetailPage newInstance(Context context,int index) {
            DetailPage detailPage = new DetailPage();
            Bundle args = new Bundle();
            Gson gson= new Gson();
            String json=gson.toJson(getCategories().get(index));
            args.putString("events", json);
            detailPage.setArguments(args);
            return detailPage;
        }

        /*private static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;


            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }

        */
        public static class Item {
            public final String title;

            private Item(String title) {
                this.title = title;
            }
        }
        /*

         public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
            private LayoutInflater inflater;
            private final List<Item> items;

            public Adapter(LayoutInflater inflater, List<Item> items) {
                this.inflater = inflater;
                this.items = items;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.detail_list_item, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.title.setText(items.get(position).title);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        }*/



    }
}
