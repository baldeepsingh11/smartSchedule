package com.example.scrollview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.scrollview.HomeFragment.layoutManager;
import static com.example.scrollview.HomeFragment.recyclerView;
import static com.example.scrollview.MainActivity.bottomNavigationView;
import static com.example.scrollview.MainActivity.dummyView;
import static com.example.scrollview.MainActivity.eventsLayout;
import static com.example.scrollview.R.drawable.blue;
import static com.example.scrollview.R.drawable.dark_blue;
import static com.example.scrollview.R.drawable.orange;
import static com.example.scrollview.R.drawable.red;

/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;
    private boolean isSpeakButtonLongPressed = false;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));
        switch (position % 4) {
            case 0:
                holder.constraintLayout.setBackground(mContext.getDrawable(orange));
                break;
            case 1:
                holder.constraintLayout.setBackground(mContext.getDrawable(blue));
                break;
            case 2:
                holder.constraintLayout.setBackground(mContext.getDrawable(dark_blue));
                break;
            case 3:
                holder.constraintLayout.setBackground(mContext.getDrawable(red));
                break;

        }
        holder.itemView.setOnLongClickListener(speakHoldListener);
        holder.itemView.setOnTouchListener(speakTouchListener);
        dummyView.setOnTouchListener(speakTouchListener);
        dummyView.setOnLongClickListener(speakHoldListener);
        holder.itemView.setHapticFeedbackEnabled(false);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));

                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EventsActivity.class);
                intent.putExtra("position", position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    List<Pair<View, String>> pairs = new ArrayList<Pair<View, String>>();
                    for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                        ViewHolder holderForAdapterPosition = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        View itemView = holderForAdapterPosition.image;
                        pairs.add(Pair.create(itemView, "tab_" + i));
                    }
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs.toArray(new Pair[]{})).toBundle();
                    mContext.startActivity(intent, bundle);
                } else {
                    mContext.startActivity(intent);
                }
            }
        });

    }

    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            dummyView.animate()
                    .setDuration(200)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            dummyView.setVisibility(View.VISIBLE);
                        }
                    });
            eventsLayout.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            eventsLayout.setVisibility(View.VISIBLE);
                        }
                    });
            bottomNavigationView.animate()
                    .translationY(bottomNavigationView.getHeight())
                    .alpha(0)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            bottomNavigationView.setVisibility(View.GONE);
                        }
                    });
            isSpeakButtonLongPressed = true;
            return true;
        }
    };
    private View.OnTouchListener speakTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isSpeakButtonLongPressed) {
                    // Do something when the button is released.
                    eventsLayout.animate()
                            .translationY(eventsLayout.getHeight())
                            .alpha(0)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    eventsLayout.setVisibility(View.GONE);
                                }
                            });
                    dummyView.animate()
                            .setDuration(200)
                            .alpha(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    dummyView.setVisibility(View.GONE);
                                }
                            });
                    bottomNavigationView.animate()
                            .translationY(0)
                            .alpha(1)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                }
                            });
                    isSpeakButtonLongPressed = false;
                }
            }
            return false;
        }
    };

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            constraintLayout = itemView.findViewById(R.id.eventLayout);
        }
    }
}
