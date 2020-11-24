package com.fmm.nowillmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class DialogPessoalActivity extends Activity implements View.OnTouchListener{

    GestureDetector gestureDetector;
    EditText fieldNome, fieldCPF;
    TextView fieldDia, fieldMes, fieldAno, txtConfirmar;
    int optionPessoal;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 250;
    private final String TAG = "UPS";
    private static final int REQUEST_CODE_SPEECH_NOME = 0,
            REQUEST_CODE_SPEECH_DIA = 1, REQUEST_CODE_SPEECH_MES = 2,
            REQUEST_CODE_SPEECH_ANO = 3, REQUEST_CODE_SPEECH_CPF = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pessoal);
        setObjects();
        gestureDetector = new GestureDetector(this, new GestureListener());
        this.setFinishOnTouchOutside(false);
    }

    private void setObjects() {
        optionPessoal = 0;
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
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionPessoal != 0) {
                optionPessoal--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionPessoal){
            case 0:
                fieldNome.setBackgroundResource(R.drawable.selectborder);
                fieldDia.setBackgroundResource(0);
                break;

            case 1:
                fieldDia.setBackgroundResource(R.drawable.selectborder);
                fieldNome.setBackgroundResource(0);
                fieldMes.setBackgroundResource(0);
                break;

            case 2:
                fieldMes.setBackgroundResource(R.drawable.selectborder);
                fieldDia.setBackgroundResource(0);
                fieldAno.setBackgroundResource(0);
                break;

            case 3:
                fieldAno.setBackgroundResource(R.drawable.selectborder);
                fieldCPF.setBackgroundResource(0);
                fieldMes.setBackgroundResource(0);
                break;

            case 4:
                fieldCPF.setBackgroundResource(R.drawable.selectborder);
                fieldAno.setBackgroundResource(0);
                txtConfirmar.setBackgroundResource(0);
                break;

            case 5:
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
            case REQUEST_CODE_SPEECH_NOME:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldNome.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_DIA:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldDia.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_MES:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldMes.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_ANO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldAno.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_CPF:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldCPF.setText(result.get(0));
                }
                break;
        }
    }
    
}
