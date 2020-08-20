package com.sunzn.curve.sample;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.sunzn.curve.library.CurveView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CurveView curve;
    private AppCompatCheckBox mGravityTop;
    private AppCompatCheckBox mGravityBtm;
    private AppCompatCheckBox mOrientationInner;
    private AppCompatCheckBox mOrientationOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGravityTop = findViewById(R.id.enable_gravity_top);
        mGravityBtm = findViewById(R.id.enable_gravity_bottom);
        mGravityTop.setOnCheckedChangeListener(this);
        mGravityBtm.setOnCheckedChangeListener(this);
        mOrientationInner = findViewById(R.id.enable_orientation_inner);
        mOrientationOuter = findViewById(R.id.enable_orientation_outer);
        mOrientationInner.setOnCheckedChangeListener(this);
        mOrientationOuter.setOnCheckedChangeListener(this);

        curve = findViewById(R.id.curve);

        DiscreteSeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                curve.setCurveHeight(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    public void colorPick(View view) {
        ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
        curve.setCurveDrawable(colorDrawable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.enable_gravity_top:
                if (isChecked) {
                    curve.setCurveGravity(CurveView.CURVE_GRAVITY_TOP);
                    mGravityBtm.setChecked(false);
                }
                break;
            case R.id.enable_gravity_bottom:
                if (isChecked) {
                    curve.setCurveGravity(CurveView.CURVE_GRAVITY_BTM);
                    mGravityTop.setChecked(false);
                }
                break;
            case R.id.enable_orientation_inner:
                if (isChecked) {
                    curve.setCurveOrientation(CurveView.CURVE_ORIENTATION_INNER);
                    mOrientationOuter.setChecked(false);
                }
                break;
            case R.id.enable_orientation_outer:
                if (isChecked) {
                    curve.setCurveOrientation(CurveView.CURVE_ORIENTATION_OUTER);
                    mOrientationInner.setChecked(false);
                }
                break;
        }
    }

}