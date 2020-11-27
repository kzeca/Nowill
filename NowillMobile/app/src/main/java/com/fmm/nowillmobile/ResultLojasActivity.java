package com.fmm.nowillmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultLojasActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;
    String base;
    TextView txtNome, txtEndereco, txtAvaliacao, txtTipo;
    DatabaseReference databaseReference;
    int optionLojas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_lojas);
        base = getIntent().getStringExtra("LOJAS");
        setAnimation();
        setObjects();
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    private void setObjects() {
        txtTipo = findViewById(R.id.activity_result_lojas_tv_tipo);
        txtNome = findViewById(R.id.activity_result_lojas_tv_nome);
        txtAvaliacao = findViewById(R.id.activity_result_lojas_tv_avaliacao);
        txtEndereco = findViewById(R.id.activity_result_lojas_tv_endereco);
        txtTipo.setText(base);
        databaseReference = FirebaseDatabase.getInstance().getReference("lojas");
        databaseReference.child(base).child(String.valueOf(optionLojas)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtNome.setText(snapshot.child("nome").getValue().toString());
                txtEndereco.setText(snapshot.child("localidade").getValue().toString());
                txtAvaliacao.setText(snapshot.child("avaliacao").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAnimation() {
        ConstraintLayout constraintLayout = findViewById(R.id.activity_result_lojas_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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

                    }
                    else {
                        // Detect rigth to left swipe
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
                        Intent intent = new Intent(ResultLojasActivity.this, LojasActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                        finish();
                    }
                }
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) ;
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            textToSpeech.stop();
            Intent intent;
            switch (optionLojas){
                case 0:
                    intent = new Intent(ResultLojasActivity.this, EstabelecimentoActivity.class);
                    intent.putExtra("RESTAURANTE", "0");
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
        }
    }
}
