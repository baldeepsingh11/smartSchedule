package com.example.scrollview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CubeOutScalingAnimation implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {

        if (position<-1){

            page.setAlpha(0);

        }

        else if (position<=0){

            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90*Math.abs(position));

        }

        else if (position<=1){

            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90*Math.abs(position));

        }

        else {

            page.setAlpha(0);
        }

        if (Math.abs(position)<=0.5) {
            page.setScaleY(Math.max(0.4f,1-Math.abs(position)));
        }
        else if(Math.abs(position)<=1){

            page.setScaleY(Math.max(0.4f,Math.abs(position)));
        }
    }


}
