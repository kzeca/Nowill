package com.fmm.nowillmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class EstabelecimentoActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
