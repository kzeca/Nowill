package com.fmm.nowillmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;

public class DialogBiometriaActivity extends AppCompatActivity implements View.OnTouchListener{

    GestureDetector gestureDetector;
    TextView fieldPalavra, fieldConfirma, fieldBiometria;
    int optionBiometria;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private final String TAG = "UPS";
    Boolean bioEx = false;
    private static final int REQUEST_CODE_SPEECH = 0;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_biometria);
        setObjects();
        biometricExists();
        gestureDetector = new GestureDetector(this, new GestureListener());
        this.setFinishOnTouchOutside(false);

    }

    private void biometricExists() {
        final BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                bioEx = true;
                fieldPalavra.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                fieldBiometria.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                fieldBiometria.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                fieldBiometria.setVisibility(View.GONE);
                break;
        }
    }

    private void setObjects() {
        optionBiometria = 0;
        fieldConfirma = findViewById(R.id.dialog_biometria_tv_confirma);
        fieldPalavra = findViewById(R.id.dialog_biometria_tv_palavra);
        fieldBiometria = findViewById(R.id.dialog_biometria_tv_verificar_biometria);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(optionBiometria != 1) {
                optionBiometria++;
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionBiometria != 0) {
                optionBiometria--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionBiometria){
            case 0:
                if(bioEx) fieldBiometria.setBackgroundResource(R.drawable.selectborder);
                else fieldPalavra.setBackgroundResource(R.drawable.selectborder);
                fieldConfirma.setBackgroundResource(0);
                break;

            case 1:
                fieldConfirma.setBackgroundResource(R.drawable.selectborder);
                if(bioEx) fieldBiometria.setBackgroundResource(0);
                else fieldPalavra.setBackgroundResource(0);
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
                        Log.d(TAG, "Right swipe ");
                    }
                    else {
                        // Detect rigth to left swipe
                        Log.d(TAG, "Left swipe ");
                    }
                }
                else if(Math.abs(valueY) > MIN_DISTANCE){

                    //Detect top to bottom swipe
                    if(y2 > y1){
                        Log.d(TAG, "Bottom swipe ");
                    }
                    else{
                        Log.d(TAG, "Top Swipe");
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
            Log.d(TAG, "Bate-dois");
            Intent intent;
            switch (optionBiometria){
                case 0:
                    if(bioEx){
                        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
                        final BiometricPrompt biometricPrompt = new BiometricPrompt(DialogBiometriaActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                            }
                        });

                        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                .setTitle("BiOMETRIA")
                                .setDescription("USANDO SUA IMPRESSÃO DIGITAL PARA LOGAR NO APLICATIVO")
                                .setNegativeButtonText("Cancel")
                                .build();
                        biometricPrompt.authenticate(promptInfo);
                    }else{
                        intent = speak();
                        try{
                            startActivityForResult(intent, REQUEST_CODE_SPEECH);
                        }catch (Exception a){
                            Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case 1:
                    finish();
                    break;
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.d(TAG, "Bate-segura");
        }
    }

    private Intent speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something");
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldPalavra.setText(result.get(0));
                }
                break;

        }
    }
}
