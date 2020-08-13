package com.example.scrollview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrollview.model.Attendence;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class userFragment extends Fragment {
    RecyclerView recyclerView;
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    ProgressBar progressBar;
    EditText status;

    public userFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        expandableView = view.findViewById(R.id.expandableView);
        arrowBtn = view.findViewById(R.id.arrowBtn);
        cardView = view.findViewById(R.id.cardView);
        recyclerView = view.findViewById(R.id.sub_recyclerview);
        status = view.findViewById(R.id.textView1);



        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

       /* List<Attendence> subjects = new ArrayList<Attendence>();

            for(int i= 0 ; i<6;i++)
            {subjects.add(new Attendence());}
*/

        SharedPreferences wmbPreference = getContext(). getSharedPreferences("com.example.scrollview",Context.MODE_PRIVATE);

        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);

        if (isFirstRun)
        {
            SharedPreferences mPrefs = getContext().getSharedPreferences("com.example.scrollview", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            Gson gson = new Gson();
            String json = gson.toJson(LoginActivity.attendences);
            prefsEditor.putString("attendence", json);
            prefsEditor.apply();

            Log.i("msg", String.valueOf(isFirstRun));
        }else{

            Log.i("msg", String.valueOf(isFirstRun));

        }
        SharedPreferences.Editor editor = wmbPreference.edit();
        editor.putBoolean("FIRSTRUN", false);
        editor.commit();





        subjectAdapter1 subjectAdapter = new subjectAdapter1(getContext(),LoginActivity.attendences);
        recyclerView.setAdapter(subjectAdapter);
        if(arrowBtn!=null) {

        }

        return view;
    }
}
