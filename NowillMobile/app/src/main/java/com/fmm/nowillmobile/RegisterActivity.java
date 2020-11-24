package com.fmm.nowillmobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import org.w3c.dom.Text;

import java.util.Locale;

public class RegisterActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private int optionRegister;
    LinearLayout fieldNome, fieldBiometria, fieldEndereco, fieldPagamento;
    TextView txtContinuar;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                textToSpeech.speak( getResources().getString(R.string.RegisterActivity_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        textToSpeech.setSpeechRate(0.8f);
        textToSpeech.setPitch(1);
        gestureDetector = new GestureDetector(this, new GestureListener());
        setObjects();
        setAnimations();
    }

    private void setAnimations() {
        LinearLayout linearLayout = findViewById(R.id.activity_register_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    private void setObjects() {
        optionRegister = 0;
        fieldNome = findViewById(R.id.activity_register_layout_nome);
        fieldBiometria = findViewById(R.id.activity_register_layout_biometria);
        fieldEndereco = findViewById(R.id.activity_register_layout_endereco);
        fieldPagamento = findViewById(R.id.activity_register_layout_pagamento);
        txtContinuar = findViewById(R.id.activity_register_tv_confirmar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(optionRegister != 4) {
                optionRegister++;
                textToSpeech.stop();
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionRegister != 0) {
                optionRegister--;
                textToSpeech.stop();
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionRegister){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.RegisterActivity_explicando_pessoal),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldNome.setBackgroundResource(R.drawable.selectborder);
                fieldBiometria.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak(getResources().getString(R.string.RegisterActivity_explicando_biometria),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldBiometria.setBackgroundResource(R.drawable.selectborder);
                fieldNome.setBackgroundResource(0);
                fieldEndereco.setBackgroundResource(0);
                break;

            case 2:
                textToSpeech.speak(getResources().getString(R.string.RegisterActivity_explicando_endereço),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldEndereco.setBackgroundResource(R.drawable.selectborder);
                fieldBiometria.setBackgroundResource(0);
                fieldPagamento.setBackgroundResource(0);
                break;

            case 3:
                textToSpeech.speak(getResources().getString(R.string.RegisterActivity_explicando_pagamento),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldPagamento.setBackgroundResource(R.drawable.selectborder);
                fieldEndereco.setBackgroundResource(0);
                txtContinuar.setBackgroundResource(0);
                break;

            case 4:
                textToSpeech.speak(getResources().getString(R.string.RegisterActivity_explicando_confirmar),
                        TextToSpeech.QUEUE_FLUSH, null);
                txtContinuar.setBackgroundResource(R.drawable.selectborder);
                fieldPagamento.setBackgroundResource(0);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent;
            Log.d("PESSOAL", "Peguei");

            switch (optionRegister) {
                case 0:
                    textToSpeech.stop();
                    intent = new Intent(RegisterActivity.this, DialogPessoalActivity.class);
                    startActivity(intent);
                    break;
                    case 1:
                        textToSpeech.stop();
                        intent = new Intent(RegisterActivity.this, DialogBiometriaActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        textToSpeech.stop();
                        Toast.makeText(getApplicationContext(), "Campo do endereço", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        textToSpeech.stop();
                        Toast.makeText(getApplicationContext(), "Campo do pagamento", Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        textToSpeech.stop();
                        intent = new Intent(RegisterActivity.this, SearchScreen.class);
                        startActivity(intent);
                        finish();
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
