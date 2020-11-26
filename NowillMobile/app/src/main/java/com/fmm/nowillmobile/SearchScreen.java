package com.fmm.nowillmobile;

import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SearchScreen extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    TextToSpeech textToSpeech;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setVoice();
        setObjects();
        gestureDetector = new GestureDetector(this, new GestureListener());
        setAnimations();
    }

    private void setObjects() {
        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
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
                textToSpeech.speak( getResources().getString(R.string.SearchScreen_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setAnimations() {
        ConstraintLayout constraintLayout = findViewById(R.id.activity_search_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            textToSpeech.stop();
            textToSpeech.speak( "Assim não vai diminuir o volume. Se deseja isso, arraste da direita para esquerda para abrir a tela" +
                            " de ajustes", TextToSpeech.QUEUE_FLUSH, null);
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            textToSpeech.stop();
            textToSpeech.speak( "Assim não vai aumentar o volume. Se deseja isso, arraste da direita para esquerda para abrir a tela" +
                            " de ajustes", TextToSpeech.QUEUE_FLUSH, null);
        }
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

                    // Detect left to right swipe
                    if(x2 > x1){
                        textToSpeech.stop();
                        Intent intent = new Intent(SearchScreen.this, DadosActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigth);
                        finish();
                    }
                    else {
                        // Detect rigth to left swipe
                        textToSpeech.stop();
                        Intent intent = new Intent(SearchScreen.this, ConfigActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                        finish();
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
                        // Detect bottom to top swipe
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
            speakSearch();
            return super.onDoubleTap(e);
        }

        void speakSearch(){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something");

            try{
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            textToSpeech.stop();
            textToSpeech.speak( getResources().getString(R.string.SearchScreen_explicando_pesquisa),
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    Intent intent = new Intent(SearchScreen.this, ResultActivity.class);
                    intent.putExtra("RESULTADO", result.get(0));
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        if(textToSpeech != null){
            textToSpeech.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onDestroy();
    }
}
