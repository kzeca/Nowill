package com.fmm.nowillmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LojasActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;
    TextView txtClasse;
    ImageView imgClasse;
    int optionClasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lojas);
        setAnimations();
        setObjects();
        setVoice();
        gestureDetector = new GestureDetector(this, new GestureListener());
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
                textToSpeech.speak( getResources().getString(R.string.LojasActivity_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionClasse = 0;
        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
        txtClasse = findViewById(R.id.activity_lojas_tv_classes);
        imgClasse = findViewById(R.id.activity_lojas_img_classe);
    }


    private void setAnimations() {
        ConstraintLayout constraintLayout = findViewById(R.id.activity_lojas_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) ;
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP);
        return true;
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
                    textToSpeech.stop();
                    // Detect left to right swipe
                    if(x2 > x1){
                        switch (optionClasse){
                            case 0:
                                optionClasse += 2;
                                txtClasse.setText("SERVIÇOS");
                                imgClasse.setImageResource(R.drawable.servicos);
                                break;
                            case 1:
                                optionClasse--;
                                txtClasse.setText("RESTAURANTES");
                                imgClasse.setImageResource(R.drawable.restaurantes);
                                break;
                            case 2:
                                optionClasse--;
                                txtClasse.setText("SUPERMERCADOS");
                                imgClasse.setImageResource(R.drawable.supermercados);
                        }
                    }
                    else {
                        // Detect rigth to left swipe
                        switch (optionClasse){
                            case 2:
                                textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_restaurantes),
                                        TextToSpeech.QUEUE_FLUSH, null);
                                optionClasse -= 2;
                                txtClasse.setText("RESTAURANTES");
                                imgClasse.setImageResource(R.drawable.restaurantes);
                                break;
                            case 1:
                                textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_serviços),
                                        TextToSpeech.QUEUE_FLUSH, null);
                                optionClasse++;
                                txtClasse.setText("SERVIÇOS");
                                imgClasse.setImageResource(R.drawable.servicos);
                                break;
                            case 0:
                                textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_supermercados),
                                        TextToSpeech.QUEUE_FLUSH, null);
                                optionClasse++;
                                txtClasse.setText("SUPERMERCADOS");
                                imgClasse.setImageResource(R.drawable.supermercados);
                        }
                    }
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
                        textToSpeech.stop();
                        Intent intent = new Intent(LojasActivity.this, SearchScreen.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                        finish();
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
            Intent intent;
            switch (optionClasse){
                case 0:
                    intent = new Intent(LojasActivity.this, ResultLojasActivity.class);
                    intent.putExtra("LOJAS", "restaurantes");
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "SUPERMERCADOS", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "SERVIÇOS", Toast.LENGTH_SHORT).show();
                    break;
            }
            return super.onDoubleTap(e);
        }


        @Override
        public void onLongPress(MotionEvent e) {
            textToSpeech.stop();
            switch (optionClasse){
                case 0:
                    textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_restaurantes),
                            TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case 1:
                    textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_supermercados),
                            TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case 2:
                    textToSpeech.speak( getResources().getString(R.string.LojasActivity_explicando_serviços),
                            TextToSpeech.QUEUE_FLUSH, null);
                    break;
            }
        }
    }


}
