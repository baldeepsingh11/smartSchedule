package com.example.scrollview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    //add button
    FloatingActionButton add ;
    static ConstraintLayout eventsLayout;
    static ImageView dummyView;
    public static BottomNavigationView bottomNavigationView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsLayout= new ConstraintLayout(this);
        eventsLayout=findViewById(R.id.event_layout);
        dummyView=findViewById(R.id.dummyView);

        loadFragment(new HomeFragment());
        add = findViewById(R.id.floatingActionButton);
        //bottom navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,reminderActivity.class));

            }
        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.action_schedule:
                            fragment = new scheduleFragment();
                            break;
                        case R.id.action_subject:
                            fragment = new subjectFragment();
                            break;
                        case R.id.action_profile:
                            fragment = new userFragment();
                    }
                    loadFragment(fragment);
                    return true;
                }
            };

    //adding fragments to viewpager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new scheduleFragment());
        viewPagerAdapter.addFragment(new subjectFragment());
        viewPagerAdapter.addFragment(new userFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
