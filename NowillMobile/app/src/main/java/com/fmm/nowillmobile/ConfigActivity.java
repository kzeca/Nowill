package com.fmm.nowillmobile;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Locale;

public class ConfigActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private int optionConfig;
    LinearLayout fieldTom, fieldVolume, fieldVelocidade;
    SharedPreferences sharedPreferences;
    private TextToSpeech textToSpeech;
    boolean click = false, volume = false, pitch = false, speed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        gestureDetector = new GestureDetector(this, new GestureListener());
        setObjects();
        setVoice();
        setAnimations();
    }

    private void setVoice() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == textToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if(result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "Language not supported");
                    }
                }else{
                    Log.e("TTS", "Initialization Failed");
                }
                textToSpeech.setSpeechRate(sharedPreferences.getFloat("voz_speed", 0.8f));
                textToSpeech.setPitch(sharedPreferences.getFloat("voz_pitch", 1));
                textToSpeech.speak( getResources().getString(R.string.ConfigActivity_intro),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionConfig = 0;
        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
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
            textToSpeech.stop();
            if(click){
                if(speed){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("voz_speed", sharedPreferences.getFloat("voz_speed", 0.8f)-0.1f);
                    editor.commit();
                    textToSpeech.setSpeechRate(sharedPreferences.getFloat("voz_speed", 0.8f));
                    textToSpeech.speak( "Testando minha velocidade de fala",
                            TextToSpeech.QUEUE_FLUSH, null);
                }else if(pitch){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("voz_speed", (sharedPreferences.getFloat("voz_pitch", 1)-0.1f));
                    editor.commit();
                    Log.d("TESTANDO", "TOM: "+sharedPreferences.getFloat("voz_pitch", 1));
                    textToSpeech.setPitch(sharedPreferences.getFloat("voz_pitch", 1));
                    textToSpeech.speak( "Testando o tom de minha voz",
                            TextToSpeech.QUEUE_FLUSH, null);
                }else if(volume){
                    textToSpeech.speak( "Testando o volume de minha voz",
                            TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            else {
                if (optionConfig != 2) {
                    optionConfig++;
                }
                setItemSelected();
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            textToSpeech.stop();
            if(click){
                if(speed){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("voz_speed", sharedPreferences.getFloat("voz_speed", 0.8f)+0.1f);
                    editor.commit();
                    textToSpeech.setSpeechRate(sharedPreferences.getFloat("voz_speed", 0.8f));
                    textToSpeech.speak( "Testando minha velocidade de fala",
                            TextToSpeech.QUEUE_FLUSH, null);
                }else if(pitch){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("voz_speed", (sharedPreferences.getFloat("voz_pitch", 1)+0.1f));
                    editor.commit();
                    Log.d("TESTANDO", "TOM: "+sharedPreferences.getFloat("voz_pitch", 1));
                    textToSpeech.setPitch(sharedPreferences.getFloat("voz_pitch", 1));
                    textToSpeech.speak( "Testando o tom de minha voz",
                            TextToSpeech.QUEUE_FLUSH, null);
                }else if(volume){
                    textToSpeech.speak( "Testando o volume de minha voz",
                            TextToSpeech.QUEUE_FLUSH, null);
                }
            }else {
                if (optionConfig != 0) {
                    optionConfig--;
                }
                setItemSelected();
            }
        }


        if(!volume) return true;
        else return super.onKeyDown(keyCode, event);
    }

    private void setItemSelected() {
        switch (optionConfig){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.ConfigActivity_explicando_volume),
                    TextToSpeech.QUEUE_FLUSH, null);
                fieldVolume.setBackgroundResource(R.drawable.selectborder);
                fieldTom.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.ConfigActivity_explicando_tom),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldTom.setBackgroundResource(R.drawable.selectborder);
                fieldVolume.setBackgroundResource(0);
                fieldVelocidade.setBackgroundResource(0);
                break;

            case 2:
                textToSpeech.speak( getResources().getString(R.string.ConfigActivity_explicando_velocidade),
                        TextToSpeech.QUEUE_FLUSH, null);
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
            textToSpeech.stop();
            if(click){
                click = false;
                volume = false;
                pitch = false;
                speed = false;

            }else{
                click = true;
                switch (optionConfig){
                    case 0:
                        volume = true;
                        break;

                    case 1:
                        pitch = true;
                        break;

                    case 2:
                        speed = true;
                        break;
                }
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if(!click){
                textToSpeech.stop();
                setItemSelected();
            }else{
                textToSpeech.speak( "Você ainda está selecionando algo. Clique duas vezes na tela e, depois disso, segure o dedo na tela " +
                                "para mais informações.",
                        TextToSpeech.QUEUE_FLUSH, null);
            }
            super.onLongPress(e);
        }
    }

    @Override
    protected void onStop() {
        if(textToSpeech != null){
            textToSpeech.stop();
        }
        super.onStop();
    }
}