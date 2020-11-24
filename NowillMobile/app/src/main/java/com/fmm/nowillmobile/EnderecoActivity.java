package com.fmm.nowillmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class EnderecoActivity extends Activity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
