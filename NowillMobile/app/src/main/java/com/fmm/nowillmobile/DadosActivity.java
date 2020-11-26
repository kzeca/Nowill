package com.fmm.nowillmobile;

import androidx.appcompat.app.AppCompatActivity;
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

public class DadosActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private int optionDados;
    LinearLayout fieldPessoal, fieldEndereco, fieldPagamento;
    private TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);
        gestureDetector = new GestureDetector(this, new GestureListener());
        setAnimations();
        setObjects();
        setVoice();
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
                textToSpeech.speak( getResources().getString(R.string.DadosActivity_intro),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionDados = 0;
        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
        fieldPessoal = findViewById(R.id.activity_dados_layout_nome);
        fieldEndereco = findViewById(R.id.activity_dados_layout_endereco);
        fieldPagamento = findViewById(R.id.activity_dados_layout_pagamento);
    }

    private void setAnimations() {
        ConstraintLayout constraintLayout = findViewById(R.id.activity_dados_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            textToSpeech.stop();
            if(optionDados != 2) {
                optionDados++;
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            textToSpeech.stop();
            if(optionDados != 0) {
                optionDados--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch(optionDados){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.DadosActivity_explicando_pessoal),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldPessoal.setBackgroundResource(R.drawable.selectborder);
                fieldEndereco.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.DadosActivity_explicando_endereco),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldEndereco.setBackgroundResource(R.drawable.selectborder);
                fieldPessoal.setBackgroundResource(0);
                fieldPagamento.setBackgroundResource(0);
                break;

            case 2:
                textToSpeech.speak( getResources().getString(R.string.DadosActivity_explicando_pagamento),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldPagamento.setBackgroundResource(R.drawable.selectborder);
                fieldPessoal.setBackgroundResource(0);
                fieldEndereco.setBackgroundResource(0);
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
                        Intent intent = new Intent(DadosActivity.this, SearchScreen.class);
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
            Intent intent;
            textToSpeech.stop();
            switch (optionDados){
                case 0:
                    intent = new Intent(DadosActivity.this, DialogPessoalActivity.class);
                    startActivity(intent);
                    break;

                case 1:
                    intent = new Intent(DadosActivity.this, DialogEnderecoActivity.class);
                    startActivity(intent);
                    break;

                case 2:
                    intent = new Intent(DadosActivity.this, DialogPagamentoActivity.class);
                    startActivity(intent);
                    break;
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            textToSpeech.stop();
            setItemSelected();
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
