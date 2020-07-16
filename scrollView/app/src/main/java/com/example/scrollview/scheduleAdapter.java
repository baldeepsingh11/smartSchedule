package com.example.scrollview;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrollview.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class scheduleAdapter extends RecyclerView.Adapter<scheduleAdapter.ViewHolder> {

    private static final String TAG = "scheduleAdapter";
    private List<Schedule> list;
    private Context mContext;

    public scheduleAdapter(Context context, List<Schedule> schedules) {
       this.list= (ArrayList<Schedule>) schedules;
       this.mContext=context;
    }

    @NonNull
    @Override
    public scheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_row, parent, false);
        return new scheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull scheduleAdapter.ViewHolder holder, int position) {
        Schedule schedule = list.get(position);
        holder.subjectCode.setText(schedule.getCode());
        holder.subjectName.setText(schedule.getName());
        holder.profName.setText(schedule.getProfName());
        holder.venue.setText(schedule.getVenue());
        holder.time.setText(schedule.getTime());
        holder.type.setText(schedule.getType());

            if (schedule.getType().equals("Tutorial")){
                holder.design.setBackground(mContext.getDrawable(R.drawable.bg_lecture));}
            else if (schedule.getType().equals("Lecture")){
                holder.design.setBackground(mContext.getDrawable(R.drawable.bg_tutorial));}
            else if(schedule.getType().equals("Practical"))
            {holder.design.setBackground(mContext.getDrawable(R.drawable.bg_practical));}



        final Button button= holder.arrowBtn;
        final ConstraintLayout constraintLayout= holder.expandableLayout;
        final CardView cardView = holder.cardView;
        final TextView subjectCode = holder.subjectCode;
        final TextView profName = holder.profName;
        final TextView type = holder.type;

        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (constraintLayout.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    Log.d(TAG, "onClick: before "+constraintLayout.getVisibility());
                    subjectCode.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    type.setVisibility(View.VISIBLE);
                    profName.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onClick: after"+ constraintLayout.getVisibility());
                    button.animate().setDuration(500).rotationXBy(180);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    subjectCode.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    profName.setVisibility(View.GONE);
                    button.animate().setDuration(500).rotationXBy(-180);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName;
        TextView venue;
        TextView time;
        ConstraintLayout expandableLayout;
        CardView cardView;
        Button arrowBtn;
        TextView subjectCode;
        TextView profName;
        TextView type;
        TextView design;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectName =itemView.findViewById(R.id.schedule_subject);
            venue = itemView.findViewById(R.id.schedule_venue);
            time = itemView.findViewById(R.id.schedule_time);
            expandableLayout = itemView.findViewById(R.id.expandable_schedule);
            cardView = itemView.findViewById(R.id.schedule_card);
            arrowBtn = itemView.findViewById(R.id.arrow_btn);
            subjectCode = itemView.findViewById(R.id.subjectCode);
            profName = itemView.findViewById(R.id.prof_name);
            type = itemView.findViewById(R.id.type);
            design = itemView.findViewById(R.id.design);
        }
    }
}
