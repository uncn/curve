package com.sunzn.curve.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.sunzn.curve.library.CurveView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CurveView curve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_gravity_top).setOnClickListener(this);

        curve = findViewById(R.id.curve);

        AppCompatSeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curve.setCurveHeight(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gravity_top:
                curve.setCurveGravity(CurveView.CURVE_GRAVITY_BTM);
                break;
        }
    }
}