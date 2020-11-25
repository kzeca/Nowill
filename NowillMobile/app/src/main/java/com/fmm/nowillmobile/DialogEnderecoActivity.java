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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_endereco);
        setObjects();
        gestureDetector = new GestureDetector(this, new GestureListener());
        this.setFinishOnTouchOutside(false);
    }

    private void setObjects() {
        optionEndereco = 0;
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
                optionEndereco++;
            }
            setItemSelected();
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(optionEndereco != 0) {
                optionEndereco--;
            }
            setItemSelected();
        }
        return true;
    }

    private void setItemSelected() {
        switch (optionEndereco){
            case 0:
                fieldRua.setBackgroundResource(R.drawable.selectborder);
                fieldBairro.setBackgroundResource(0);
                break;

            case 1:
                fieldBairro.setBackgroundResource(R.drawable.selectborder);
                fieldRua.setBackgroundResource(0);
                fieldNumero.setBackgroundResource(0);
                break;

            case 2:
                fieldNumero.setBackgroundResource(R.drawable.selectborder);
                fieldCEP.setBackgroundResource(0);
                fieldBairro.setBackgroundResource(0);
                break;

            case 3:
                fieldCEP.setBackgroundResource(R.drawable.selectborder);
                fieldNumero.setBackgroundResource(0);
                fieldConfirmar.setBackgroundResource(0);
                break;

            case 4:
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
            case REQUEST_CODE_SPEECH_RUA:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldRua.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_BAIRRO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldBairro.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_CEP:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldCEP.setText(result.get(0));
                }
                break;

            case REQUEST_CODE_SPEECH_NUMERO:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("BAH", result.get(0));
                    fieldNumero.setText(result.get(0));
                }
                break;
        }
    }

}
