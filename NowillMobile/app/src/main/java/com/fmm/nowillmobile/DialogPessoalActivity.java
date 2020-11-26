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
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class DialogPessoalActivity extends Activity implements View.OnTouchListener{

    GestureDetector gestureDetector;
    EditText fieldNome, fieldCPF;
    TextView fieldDia, fieldMes, fieldAno, txtConfirmar;
    int optionPessoal;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 260;
    private final String TAG = "UPS";
    private static final int REQUEST_CODE_SPEECH_NOME = 0,
            REQUEST_CODE_SPEECH_DIA = 1, REQUEST_CODE_SPEECH_MES = 2,
            REQUEST_CODE_SPEECH_ANO = 3, REQUEST_CODE_SPEECH_CPF = 4;
    private TextToSpeech textToSpeech;
    RegisterActivity register;
    boolean registerScreen;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pessoal);
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
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_intro), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setObjects() {
        optionPessoal = 0;
        register = new RegisterActivity();
        sharedPreferences = getSharedPreferences("MyUserSharedPreferences", Context.MODE_PRIVATE);
        fieldNome = findViewById(R.id.dialog_pessoal_et_nome);
        fieldDia = findViewById(R.id.dialog_pessoal_tv_dia);
        fieldMes = findViewById(R.id.dialog_pessoal_tv_mes);
        fieldAno = findViewById(R.id.dialog_pessoal_tv_ano);
        fieldCPF = findViewById(R.id.dialog_pessoal_et_cpf);
        txtConfirmar = findViewById(R.id.dialog_pessoal_tv_confirma);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(optionPessoal != 5) {
                optionPessoal++;
                textToSpeech.stop();
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionPessoal != 0) {
                optionPessoal--;
                textToSpeech.stop();
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionPessoal){
            case 0:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_nome),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldNome.setBackgroundResource(R.drawable.selectborder);
                fieldDia.setBackgroundResource(0);
                break;

            case 1:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_dia),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldDia.setBackgroundResource(R.drawable.selectborder);
                fieldNome.setBackgroundResource(0);
                fieldMes.setBackgroundResource(0);
                break;

            case 2:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_mes),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldMes.setBackgroundResource(R.drawable.selectborder);
                fieldDia.setBackgroundResource(0);
                fieldAno.setBackgroundResource(0);
                break;

            case 3:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_ano),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldAno.setBackgroundResource(R.drawable.selectborder);
                fieldCPF.setBackgroundResource(0);
                fieldMes.setBackgroundResource(0);
                break;

            case 4:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_cpf),
                        TextToSpeech.QUEUE_FLUSH, null);
                fieldCPF.setBackgroundResource(R.drawable.selectborder);
                fieldAno.setBackgroundResource(0);
                txtConfirmar.setBackgroundResource(0);
                break;

            case 5:
                textToSpeech.speak( getResources().getString(R.string.DialogPessoalActivity_explicando_confirmar),
                        TextToSpeech.QUEUE_FLUSH, null);
                txtConfirmar.setBackgroundResource(R.drawable.selectborder);
                fieldCPF.setBackgroundResource(0);
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
            switch (optionPessoal){
                case 0:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_NOME);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 1:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_DIA);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_MES);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 3:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_ANO);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 4:
                    intent = speak();
                    try{
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_CPF);
                    }catch (Exception a){
                        Toast.makeText(getApplicationContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 5:
                    if(registerScreen) {
                        register.users.setNome(fieldNome.getText().toString());
                        register.users.setNascimento(fieldDia.getText().toString() + " " + fieldMes.getText().toString() + " "
                                + fieldAno.getText().toString());
                        register.users.setCpf(fieldCPF.getText().toString());
                        finish();
                    }else{
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
                        databaseReference.child(android_id).child("pessoal").child("nome").setValue(fieldNome.getText().toString());
                        databaseReference.child(android_id).child("pessoal").child("cpf").setValue(fieldCPF.getText().toString());
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
            case REQUEST_CODE_SPEECH_NOME:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String nome = result.get(0);
                    textToSpeech.speak("Seu nome é: "+result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    fieldNome.setText(nome.trim());
                }
                break;

            case REQUEST_CODE_SPEECH_DIA:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String dia = result.get(0);
                    textToSpeech.speak("O dia do seu nascimento é: "+result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    fieldDia.setText(dia.trim());
                }
                break;

            case REQUEST_CODE_SPEECH_MES:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String mes = result.get(0);
                    textToSpeech.speak("O mês do seu nascimento é: "+result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    fieldMes.setText(mes.trim());
                }
                break;

            case REQUEST_CODE_SPEECH_ANO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ano = result.get(0);
                    textToSpeech.speak("O ano do seu nascimento é: "+result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    fieldAno.setText(ano.trim());
                }
                break;

            case REQUEST_CODE_SPEECH_CPF:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String cpf = result.get(0);
                    cpf = cpf.replace(" ", "");
                    cpf = cpf.replace("/", "");
                    cpf = cpf.replace("e", "");
                    cpf = cpf.replace("d", "");
                    textToSpeech.speak("O seu c.p.f. é, em número: "+cpf, TextToSpeech.QUEUE_FLUSH, null);
                    fieldCPF.setText(cpf.trim());
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
                    fieldNome.setText(snapshot.child("pessoal").child("nome").getValue().toString());
                    fieldCPF.setText(snapshot.child("pessoal").child("cpf").getValue().toString());
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
