package com.sunzn.curve.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CurveView extends View {

    private CurveHelper helper;

    public static final int CURVE_GRAVITY_TOP = 0;
    public static final int CURVE_GRAVITY_BTM = 1;

    public static final int CURVE_ORIENTATION_INNER = 0;
    public static final int CURVE_ORIENTATION_OUTER = 1;

    public CurveView(Context context) {
        this(context, null);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new CurveHelper(this, context, attrs, defStyleAttr);
    }

    public void setCurveHeight(int height) {
        helper.setCurveHeight(height);
    }

    public void setCurveGravity(int gravity) {
        helper.setCurveGravity(gravity);
    }

    public void setCurveOrientation(int orientation) {
        helper.setCurveOrientation(orientation);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        helper.onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        helper.onDraw(canvas);
    }

}
