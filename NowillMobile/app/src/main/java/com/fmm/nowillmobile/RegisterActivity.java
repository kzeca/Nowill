package com.fmm.nowillmobile;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class RegisterActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private int i;
    LinearLayout fieldNome, fieldBiometria, fieldEndereco, fieldPagamento;
    TextView txtContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        i = 0;
        fieldNome = findViewById(R.id.activity_register_layout_nome);
        fieldBiometria = findViewById(R.id.activity_register_layout_biometria);
        fieldEndereco = findViewById(R.id.activity_register_layout_endereco);
        fieldPagamento = findViewById(R.id.activity_register_layout_pagamento);
        txtContinuar = findViewById(R.id.activity_register_tv_confirmar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(i != 4) i++;
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(i != 0) i--;
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (i){
            case 0:
                fieldNome.setBackgroundResource(R.drawable.selectborder);
                fieldBiometria.setBackgroundResource(0);
                break;

            case 1:
                fieldBiometria.setBackgroundResource(R.drawable.selectborder);
                fieldNome.setBackgroundResource(0);
                fieldEndereco.setBackgroundResource(0);
                break;

            case 2:
                fieldEndereco.setBackgroundResource(R.drawable.selectborder);
                fieldBiometria.setBackgroundResource(0);
                fieldPagamento.setBackgroundResource(0);
                break;

            case 3:
                fieldPagamento.setBackgroundResource(R.drawable.selectborder);
                fieldEndereco.setBackgroundResource(0);
                txtContinuar.setBackgroundResource(0);
                break;

            case 4:
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
            switch (i){
                case 0:
                    Toast.makeText(getApplicationContext(), "Campo do nome", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Campo da biometria", Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "Campo do endereço", Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    Toast.makeText(getApplicationContext(), "Campo do pagamento", Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return super.onDoubleTap(e);
        }

    }

}
