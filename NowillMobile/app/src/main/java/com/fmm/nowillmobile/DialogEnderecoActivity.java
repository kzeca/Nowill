package com.fmm.nowillmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class DialogEnderecoActivity extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;
    TextView fieldRua, fieldBairro, fieldNumero, fieldCEP, fieldConfirmar;
    int optionEndereco;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private final String TAG = "UPS";
    private static final int REQUEST_CODE_SPEECH_RUA = 0,
            REQUEST_CODE_SPEECH_BAIRRO = 1, REQUEST_CODE_SPEECH_NUMERO = 2,
            REQUEST_CODE_SPEECH_CEP= 3;
    private TextToSpeech textToSpeech;
    RegisterActivity register;
    boolean registerScreen;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_endereco);
        setVoice();
        setObjects();
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
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionEndereco = 0;
        register = new RegisterActivity();
        sharedPreferences = getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
        fieldRua = findViewById(R.id.dialog_endereco_tv_rua);
        fieldBairro = findViewById(R.id.dialog_endereco_tv_bairro);
        fieldNumero = findViewById(R.id.dialog_endereco_tv_numero);
        fieldCEP = findViewById(R.id.dialog_endereco_tv_cep);
        fieldConfirmar = findViewById(R.id.dialog_endereco_tv_confirma);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(optionEndereco != 4) {
                textToSpeech.stop();
                optionEndereco++;
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionEndereco != 0) {
                textToSpeech.stop();
                optionEndereco--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionEndereco){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_explicando_rua),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldRua.setBackgroundResource(R.drawable.selectborder);
                fieldBairro.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_explicando_bairro),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldBairro.setBackgroundResource(R.drawable.selectborder);
                fieldRua.setBackgroundResource(0);
                fieldNumero.setBackgroundResource(0);
                break;

            case 2:
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_explicando_numero),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldNumero.setBackgroundResource(R.drawable.selectborder);
                fieldCEP.setBackgroundResource(0);
                fieldBairro.setBackgroundResource(0);
                break;

            case 3:
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_explicando_CEP),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldCEP.setBackgroundResource(R.drawable.selectborder);
                fieldNumero.setBackgroundResource(0);
                fieldConfirmar.setBackgroundResource(0);
                break;

            case 4:
                textToSpeech.speak( getResources().getString(R.string.DialogEnderecoActivity_explicando_confirmar),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldConfirmar.setBackgroundResource(R.drawable.selectborder);
                fieldCEP.setBackgroundResource(0);
                break;
        }
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
            Intent intent;
            textToSpeech.stop();
            switch (optionEndereco){
                case 0:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_RUA);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 1:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_BAIRRO);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_NUMERO);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 3:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_CEP);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;


                case 4:
                    if(registerScreen) {
                        register.users.setRua(fieldRua.getText().toString());
                        register.users.setBairro(fieldBairro.getText().toString());
                        register.users.setNumero(fieldNumero.getText().toString());
                        register.users.setCep(fieldCEP.getText().toString());
                        finish();
                    }else{
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                        databaseReference.child(android_id).child("endereco").child("rua").setValue(fieldRua.getText().toString());
                        databaseReference.child(android_id).child("endereco").child("bairro").setValue(fieldBairro.getText().toString());
                        databaseReference.child(android_id).child("endereco").child("cep").setValue(fieldCEP.getText().toString());
                        databaseReference.child(android_id).child("endereco").child("numero").setValue(fieldNumero.getText().toString());
                        finish();
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
            case REQUEST_CODE_SPEECH_RUA:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String rua = result.get(0);
                    textToSpeech.speak("A rua da residência é: "+rua,
                            TextToSpeech.QUEUE_FLUSH, null);
                    rua = rua.trim();
                    fieldRua.setText(rua);
                }
                break;

            case REQUEST_CODE_SPEECH_BAIRRO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String bairro = result.get(0);
                    bairro = bairro.trim();
                    textToSpeech.speak("O bairro da residência é: "+bairro,
                            TextToSpeech.QUEUE_FLUSH, null);
                    fieldBairro.setText(bairro);
                }
                break;

            case REQUEST_CODE_SPEECH_CEP:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String cep = result.get(0);
                    cep = cep.trim();
                    cep = cep.replace(" ", "");
                    cep = cep.replace("-", "");
                    cep = cep.replace("/", "");
                    cep = cep.replace("e", "");
                    textToSpeech.speak("O C.E.P. da residência é: "+cep,
                            TextToSpeech.QUEUE_FLUSH, null);
                    fieldCEP.setText(cep);
                }
                break;

            case REQUEST_CODE_SPEECH_NUMERO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String numero = result.get(0);
                    numero = numero.trim();
                    textToSpeech.speak("O número da residência é: "+numero,
                            TextToSpeech.QUEUE_FLUSH, null);
                    fieldNumero.setText(numero);
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
    protected void onStart() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").child(android_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    fieldRua.setText(snapshot.child("endereco").child("rua").getValue().toString());
                    fieldBairro.setText(snapshot.child("endereco").child("bairro").getValue().toString());
                    fieldCEP.setText(snapshot.child("endereco").child("cep").getValue().toString());
                    fieldNumero.setText(snapshot.child("endereco").child("numero").getValue().toString());
                    registerScreen = false;
                }else {
                    registerScreen = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }

}
