package com.fmm.nowillmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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

public class DialogEnterActivity extends AppCompatActivity implements View.OnTouchListener{

    GestureDetector gestureDetector;
    TextView fieldPalavra, fieldBiometria, fieldConfirma;
    int optionEnter = 0;
    private final String TAG = "UPS";
    Boolean bioEx;
    private static final int REQUEST_CODE_SPEECH = 0;
    private TextToSpeech textToSpeech;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_enter);
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
                textToSpeech.setSpeechRate(sharedPreferences.getFloat("voz_speed", 0.8f));
                textToSpeech.setPitch(sharedPreferences.getFloat("voz_pitch", 1));
                if(bioEx) textToSpeech.speak( getResources().getString(R.string.DialogEnterActivity_intro_cbio),
                        TextToSpeech.QUEUE_FLUSH, null);
                else textToSpeech.speak( getResources().getString(R.string.DialogEnterActivity_intro_sbio),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }


    private void setObjects() {
        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
        fieldBiometria = findViewById(R.id.dialog_enter_tv_verificar_biometria);
        fieldPalavra = findViewById(R.id.dialog_enter_tv_palavra);
        fieldConfirma = findViewById(R.id.dialog_enter_tv_confirma);
        bioEx = sharedPreferences.getBoolean("BIOMETRIA", false);
        if(bioEx){
            fieldPalavra.setVisibility(View.GONE);
            fieldConfirma.setVisibility(View.GONE);
        } else fieldBiometria.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(!bioEx) {
                if (optionEnter != 1) {
                    optionEnter++;
                    textToSpeech.stop();

                }
                setItemSelected();
            }
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(!bioEx) {
                if (optionEnter != 0) {
                    optionEnter--;
                    textToSpeech.stop();

                }
                setItemSelected();
            }
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionEnter){
            case 0:
                if(bioEx) {
                    fieldBiometria.setBackgroundResource(R.drawable.selectborder);
                    textToSpeech.speak( getResources().getString(R.string.DialogEnterActivity_intro_cbio),
                            TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    fieldPalavra.setBackgroundResource(R.drawable.selectborder);
                    textToSpeech.speak( getResources().getString(R.string.DialogEnterActivity_intro_sbio),
                            TextToSpeech.QUEUE_FLUSH, null);
                }
                fieldConfirma.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.DialogEnterActivity_explicando_confirmar),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldConfirma.setBackgroundResource(R.drawable.selectborder);
                if(bioEx) fieldBiometria.setBackgroundResource(0);
                else fieldPalavra.setBackgroundResource(0);
                break;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent;
            textToSpeech.stop();
            switch (optionEnter){
                case 0:
                    if(bioEx){
                        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
                        final BiometricPrompt biometricPrompt = new BiometricPrompt(DialogEnterActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                Intent intent1 = new Intent(DialogEnterActivity.this, SearchScreen.class);
                                startActivity(intent1);
                                super.onAuthenticationSucceeded(result);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                textToSpeech.speak( "Você errou. Tente Novamente!",
                                        TextToSpeech.QUEUE_FLUSH, null);
                                super.onAuthenticationFailed();
                            }
                        });

                        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                .setTitle("BIOMETRIA")
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
                    if(fieldPalavra.getText().toString().equals(sharedPreferences.getString("Palavra-Passe", ""))){
                        intent = new Intent(DialogEnterActivity.this, SearchScreen.class);
                        startActivity(intent);
                        finish();
                    }else{
                        textToSpeech.speak( "Você errou. Tente Novamente!",
                                TextToSpeech.QUEUE_FLUSH, null);
                    }
                    break;
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            textToSpeech.stop();
            setItemSelected();
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
                    String palavra = result.get(0).trim();
                    palavra = palavra.toLowerCase();
                    palavra = palavra.replace(" ", "");
                    fieldPalavra.setText(palavra);
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

}
