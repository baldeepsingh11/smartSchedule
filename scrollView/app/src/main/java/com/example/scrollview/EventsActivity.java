package com.example.scrollview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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


import com.example.scrollview.model.events;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

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
            return DetailPage.instantiate(context, DetailPage.class.getName());
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
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.detail_page, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            list.setAdapter(new Adapter(LayoutInflater.from(getContext()), new ArrayList<Item>() {
                {
                    for (int i = 0; i < 30; i++) {
                        add(new Item("detail:" + i));
                    }
                }
            }));
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }

        public static class Item {
            public final String title;

            private Item(String title) {
                this.title = title;
            }
        }

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
        }

    }
}
