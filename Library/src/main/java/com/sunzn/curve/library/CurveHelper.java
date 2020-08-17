package com.sunzn.curve.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import static android.view.View.LAYER_TYPE_SOFTWARE;
import static com.sunzn.curve.library.CurveView.CURVE_GRAVITY_BTM;
import static com.sunzn.curve.library.CurveView.CURVE_GRAVITY_TOP;
import static com.sunzn.curve.library.CurveView.CURVE_ORIENTATION_INNER;
import static com.sunzn.curve.library.CurveView.CURVE_ORIENTATION_OUTER;

public class CurveHelper {

    private Context context;

    private CurveView view;

    private Path mPath;
    private Paint mPaint;
    private PorterDuffXfermode mMode;

    private int mCurveHeight;
    private int mCurveGravity;
    private int mCurveOrientation;
    private Drawable mcCurveBackground;

    private int mW, mH;
    private float startAngle, sweepAngle;
    private float left, top, right, bottom;
    private double width;
    private int height;

    public CurveHelper(CurveView view, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.view = view;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CurveView, defStyleAttr, 0);
        mCurveGravity = attributes.getInt(R.styleable.CurveView_curve_gravity, CURVE_GRAVITY_TOP);
        mCurveHeight = attributes.getDimensionPixelSize(R.styleable.CurveView_curve_height, 50);
        mCurveOrientation = attributes.getInt(R.styleable.CurveView_curve_orientation, CURVE_ORIENTATION_OUTER);
        mcCurveBackground = attributes.getDrawable(R.styleable.CurveView_curve_background);
        attributes.recycle();
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        view.setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setCurveHeight(int height) {
        mCurveHeight = dp2px(height);
        math(mW, mH);
        invalidate();
    }

    public void setCurveGravity(int gravity) {
        mCurveGravity = gravity;
        math(mW, mH);
        invalidate();
    }

    public void setCurveOrientation(int orientation) {
        mCurveOrientation = orientation;
        math(mW, mH);
        invalidate();
    }

    public void onSizeChanged(int w, int h) {
        mH = h;
        mW = w;
        math(mW, mH);
    }

    public void onDraw(Canvas canvas) {
        int save = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        mPath.reset();
        drawCtxBitmap(canvas);
        mPaint.setXfermode(mMode);

        switch (mCurveGravity) {
            case CURVE_GRAVITY_TOP:
                switch (mCurveOrientation) {
                    case CURVE_ORIENTATION_INNER:
                        mPath.arcTo(left, top, right, bottom, startAngle, sweepAngle, true);
                        canvas.drawPath(mPath, mPaint);
                        break;
                    case CURVE_ORIENTATION_OUTER:
                        mPaint.setColor(Color.RED);
                        mPath.moveTo((float) (width * 2), height);
                        mPath.lineTo((float) (width * 2), 0);
                        mPath.lineTo(0, 0);
                        mPath.lineTo(0, height);
                        mPath.arcTo(left, top, right, bottom, startAngle, sweepAngle, false);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);
                        break;
                }
                break;
            case CURVE_GRAVITY_BTM:
                switch (mCurveOrientation) {
                    case CURVE_ORIENTATION_INNER:
                        mPath.arcTo(left, top, right, bottom, startAngle, sweepAngle, true);
                        canvas.drawPath(mPath, mPaint);
                        break;
                    case CURVE_ORIENTATION_OUTER:
                        mPaint.setColor(Color.RED);
                        mPath.moveTo((float) (width * 2), mH - height);
                        mPath.arcTo(left, top, right, bottom, startAngle, sweepAngle, false);
                        mPath.lineTo(0, mH);
                        mPath.lineTo((float) (width * 2), mH);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);
                        break;
                }
                break;
        }

        mPaint.setXfermode(null);
        canvas.restoreToCount(save);
    }

    private void drawCtxBitmap(Canvas canvas) {
        if (mcCurveBackground instanceof ColorDrawable) {
            RectF rectF = new RectF(0, 0, mW, mH);
            mPaint.setColor(((ColorDrawable) mcCurveBackground).getColor());
            canvas.drawRect(rectF, mPaint);
        } else {
            Bitmap bitmap = ((BitmapDrawable) mcCurveBackground).getBitmap();
            int height = bitmap.getHeight() * mW / bitmap.getWidth();
            RectF rectF = new RectF(0, 0, mW, height);
            canvas.drawBitmap(bitmap, null, rectF, null);
        }
    }

    private void math(int w, int h) {
        double radius;
        switch (mCurveGravity) {
            case CURVE_GRAVITY_TOP:
                switch (mCurveOrientation) {
                    case CURVE_ORIENTATION_INNER:
                        width = w / 2D;
                        height = Math.min(mCurveHeight, (int) width);
                        radius = (width * width + height * height) / (2 * height);
                        startAngle = (float) (Math.acos(width / radius) * 180 / Math.PI);
                        sweepAngle = 180 - 2 * startAngle;
                        left = (float) (width - radius);
                        top = (float) (height - 2 * radius);
                        right = (float) (width + radius);
                        bottom = height;
                        break;
                    case CURVE_ORIENTATION_OUTER:
                        width = w / 2D;
                        height = Math.min(mCurveHeight, (int) width);
                        radius = (width * width + height * height) / (2 * height);
                        startAngle = (float) (Math.acos(width / radius) * 180 / Math.PI) - 180;
                        sweepAngle = 180 - startAngle * 2;
                        left = (float) (width - radius);
                        top = 0;
                        right = (float) (width + radius);
                        bottom = (float) (radius * 2);
                        break;
                }
                break;
            case CURVE_GRAVITY_BTM:
                switch (mCurveOrientation) {
                    case CURVE_ORIENTATION_INNER:
                        width = w / 2D;
                        height = Math.min(mCurveHeight, (int) width);
                        radius = (width * width + height * height) / (2 * height);
                        startAngle = (float) (Math.acos(width / radius) * 180 / Math.PI) - 180;
                        sweepAngle = 180 - startAngle * 2;
                        left = (float) (width - radius);
                        top = h - height;
                        right = (float) (width + radius);
                        bottom = (float) (radius * 2) + h - height;
                        break;
                    case CURVE_ORIENTATION_OUTER:
                        width = w / 2D;
                        height = Math.min(mCurveHeight, (int) width);
                        radius = (width * width + height * height) / (2 * height);
                        startAngle = (float) (Math.acos(width / radius) * 180 / Math.PI);
                        sweepAngle = 180 - 2 * startAngle;
                        left = (float) (width - radius);
                        top = (float) (h - 2 * radius);
                        right = (float) (width + radius);
                        bottom = h;
                        break;
                }
                break;
        }
    }

    private void invalidate() {
        if (view != null) view.invalidate();
    }

    private int dp2px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

}
