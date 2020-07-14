package com.example.scrollview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scrollview.model.Tasks;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mTasks = new ArrayList<>();
    private ArrayList<String> mTaskImages = new ArrayList<>();
    private ArrayList<String> mTaskDate = new ArrayList<>();
    private List<Tasks> tasks;
    private Context mContext;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String datetimeString(Tasks tasks)
    {
        Date date = tasks.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM", Locale.getDefault());
        String strDate = dateFormat.format(date);


        return strDate + " " + changeFormat(date.getHours(),date.getMinutes());
    }
    private String changeFormat(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }  //converting to 12 hour format


    public TaskRecyclerViewAdapter(ArrayList<String> mTasks, ArrayList<String> mTaskImages, ArrayList<String> mTaskDate, Context mContext, List<Tasks> tasks) {
        this.tasks =tasks;
        this.mTasks = mTasks;
        this.mTaskImages = mTaskImages;
        this.mTaskDate = mTaskDate;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
        return new TaskRecyclerViewAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.ViewHolder holder, int position) {

       /* Glide.with(mContext)
                .asBitmap()
                .load(mTaskImages.get(position))
                .into(holder.image);*/


        Tasks task = tasks.get(position);
        holder.image.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.calender_icon, null));
        holder.title.setText(task.getTitle());
        holder.date.setText(datetimeString(task));
        holder.linearLayout.setBackground(mContext.getDrawable(R.color.grey));

    }

    @Override
    public int getItemCount() {
        return mTaskImages.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView date;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.taskImage);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            linearLayout = itemView.findViewById(R.id.taskItemLayout);
        }
    }
}
