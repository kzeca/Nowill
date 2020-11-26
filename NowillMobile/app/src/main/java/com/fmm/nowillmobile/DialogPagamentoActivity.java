package com.fmm.nowillmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DialogPagamentoActivity extends Activity implements View.OnTouchListener{

    GestureDetector gestureDetector;
    LinearLayout fieldDinheiro, fieldCartao, fieldForma;
    TextView fieldConfirma;
    int optionPagamento, optionForma;
    boolean formaSelected;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 260;
    private final String TAG = "UPS";
    private TextToSpeech textToSpeech;
    RegisterActivity register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_pagamento);
        setObjects();
        setVoice();
        gestureDetector = new GestureDetector(this, new GestureListener());
        this.setFinishOnTouchOutside(false);
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
                textToSpeech.setSpeechRate(0.8f);
                textToSpeech.setPitch(1);
                textToSpeech.speak( getResources().getString(R.string.DialogPagamentoActivity_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionPagamento = 0;
        optionForma = 1;
        register = new RegisterActivity();
        fieldCartao = findViewById(R.id.dialog_pagamento_layout_cartao);
        fieldDinheiro = findViewById(R.id.dialog_pagamento_layout_dinheiro);
        fieldConfirma = findViewById(R.id.dialog_pagamento_tv_confirma);
        fieldForma = findViewById(R.id.dialog_pagamento_layout_forma);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(!formaSelected) {
                if (optionPagamento != 1) {
                    textToSpeech.stop();
                    optionPagamento++;
                }
                setItemSelected();
            }
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(!formaSelected) {
                if (optionPagamento != 0) {
                    textToSpeech.stop();
                    optionPagamento--;
                }
                setItemSelected();
            }
        }
        return true;
    }


    private void setItemSelected() {
        switch (optionPagamento){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.DialogPagamentoActivity_explicando_pagamento),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldForma.setBackgroundResource(R.drawable.selectborder);
                fieldConfirma.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.DialogPagamentoActivity_explicando_confirmar),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldConfirma.setBackgroundResource(R.drawable.selectborder);
                fieldForma.setBackgroundResource(0);
                break;
        }
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
                        if(formaSelected){
                            fieldCartao.setBackgroundResource(R.drawable.selectborder);
                            fieldDinheiro.setBackgroundResource(0);
                            optionForma = 0;
                        }
                    }
                    else {
                        // Detect rigth to left swipe
                        if(formaSelected){
                            fieldDinheiro.setBackgroundResource(R.drawable.selectborder);
                            fieldCartao.setBackgroundResource(0);
                            optionForma = 1;
                        }
                    }
                }
                else if(Math.abs(valueY) > MIN_DISTANCE){

                    //Detect top to bottom swipe
                    if(y2 > y1){
                        Log.d(TAG, "Bottom swipe ");
                    }
                    else{
                        textToSpeech.stop();
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
            switch (optionPagamento){
                case 0:
                    if(formaSelected) formaSelected = false;
                    else formaSelected = true;
                    break;

                case 1:
                    if(optionForma == 1) register.users.setPagamento("Dinheiro");
                    else register.users.setPagamento("Cartão");
                    finish();
                    break;
            }

            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if(!formaSelected){
                textToSpeech.stop();
                setItemSelected();
            }
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
