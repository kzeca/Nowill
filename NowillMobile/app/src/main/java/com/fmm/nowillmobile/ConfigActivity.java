package com.fmm.nowillmobile;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ConfigActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private int optionConfig;
    LinearLayout fieldTom, fieldVolume, fieldVelocidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        gestureDetector = new GestureDetector(this, new GestureListener());
        setObjects();
        setAnimations();
    }

    private void setObjects() {
        optionConfig = 0;
        fieldVolume = findViewById(R.id.activity_config_layout_volume);
        fieldTom = findViewById(R.id.activity_config_layout_tom);
        fieldVelocidade = findViewById(R.id.activity_config_layout_velocidade);
    }

    private void setAnimations() {
        ConstraintLayout constraintLayout = findViewById(R.id.activity_config_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(optionConfig != 2) {
                optionConfig++;
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionConfig != 0) {
                optionConfig--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionConfig){
            case 0:
                fieldVolume.setBackgroundResource(R.drawable.selectborder);
                fieldTom.setBackgroundResource(0);
                break;

            case 1:
                fieldTom.setBackgroundResource(R.drawable.selectborder);
                fieldVolume.setBackgroundResource(0);
                fieldVelocidade.setBackgroundResource(0);
                break;

            case 2:
                fieldVelocidade.setBackgroundResource(R.drawable.selectborder);
                fieldVolume.setBackgroundResource(0);
                fieldTom.setBackgroundResource(0);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            //starting swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            //ending swipe time gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                //getting value from horizontal swipe
                float valueX = x2 - x1;
                //getting value from vertical swipe
                float valueY = y2 - y1;

                if(Math.abs(valueX) > MIN_DISTANCE){
                    /*
                    // Detect left to right swipe
                    if(x2 > x1){
                        Intent intent = new Intent(CenterActivity.this, LeftActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigth);
                        Log.d(TAG, "Right swipe ");
                    }
                    else {
                        // Detect rigth to left swipe

                        Intent intent = new Intent(CenterActivity.this, RightActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                        Log.d(TAG, "Left swipe ");
                    }*/
                }
                else if(Math.abs(valueY) > MIN_DISTANCE){

                    //Detect top to bottom swipe
                    if(y2 > y1){/*
                        Intent intent = new Intent(CenterActivity.this, UpActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                        Log.d(TAG, "Bottom swipe ");*/
                    }
                    else{
                        // Detect bottom to top swipe
                        Intent intent = new Intent(ConfigActivity.this, SearchScreen.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                    }
                }
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    }
}
