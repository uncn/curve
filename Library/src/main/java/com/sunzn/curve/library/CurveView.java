package com.sunzn.curve.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CurveView extends View {

    private final int CURVE_HEIGHT = 50;

    private final int CURVE_GRAVITY_TOP = 0;
    private final int CURVE_GRAVITY_BTM = 1;

    private final int CURVE_ORIENTATION_INNER = 0;
    private final int CURVE_ORIENTATION_OUTER = 1;

    private int mCurveHeight;
    private int mCurveGravity;
    private int mCurveOrientation;
    private Drawable mcCurveBackground;

    private Path mPath;
    private Paint mPaint;
    private PorterDuffXfermode mMode;
    private PaintFlagsDrawFilter mFilter;

    public CurveView(Context context) {
        this(context, null);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CurveView);
        mCurveGravity = attributes.getInt(R.styleable.CurveView_curve_gravity, CURVE_GRAVITY_TOP);
        mCurveHeight = attributes.getDimensionPixelOffset(R.styleable.CurveView_curve_height, CURVE_HEIGHT);
        mCurveOrientation = attributes.getInt(R.styleable.CurveView_curve_orientation, CURVE_ORIENTATION_OUTER);
        mcCurveBackground = attributes.getDrawable(R.styleable.CurveView_curve_background);
        attributes.recycle();
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int save = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        drawCtxBitmap(canvas);
        mPaint.setXfermode(mMode);
        drawArcBitmap(canvas);
        mPaint.setXfermode(null);
        canvas.restoreToCount(save);
    }

    private void drawCtxBitmap(Canvas canvas) {
        if (mcCurveBackground instanceof ColorDrawable) {
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            mPaint.setColor(((ColorDrawable) mcCurveBackground).getColor());
            canvas.drawRect(rectF, mPaint);
        } else {
            Bitmap bitmap = ((BitmapDrawable) mcCurveBackground).getBitmap();
            RectF rectF = new RectF(0, 0, getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, null, rectF, null);
        }
    }

    private void drawArcBitmap(Canvas canvas) {
        canvas.setDrawFilter(mFilter);
        initArcPath();
        canvas.drawPath(mPath, mPaint);
    }

    private void initArcPath() {
        float anchor = getWidth() / 2F;
        mPath.moveTo(0, getStartY());
        mPath.lineTo(0, getArStartY());
        mPath.quadTo(anchor, getCurveY(), getWidth(), getCurveEndY());
        mPath.lineTo(getWidth(), getArcEndY());
        mPath.close();
    }

    private int getStartY() {
        return mCurveGravity == CURVE_GRAVITY_TOP ? 0 : getHeight();
    }

    private int getArStartY() {
        switch (mCurveGravity) {
            case CURVE_GRAVITY_TOP:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? 0 : mCurveHeight;
            case CURVE_GRAVITY_BTM:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? getHeight() : getHeight() - mCurveHeight;
            default:
                return 0;
        }
    }

    private int getCurveY() {
        switch (mCurveGravity) {
            case CURVE_GRAVITY_TOP:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? mCurveHeight : 0;
            case CURVE_GRAVITY_BTM:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? getHeight() - mCurveHeight : getHeight();
            default:
                return 0;
        }
    }

    private int getCurveEndY() {
        switch (mCurveGravity) {
            case CURVE_GRAVITY_TOP:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? 0 : mCurveHeight;
            case CURVE_GRAVITY_BTM:
                return mCurveOrientation == CURVE_ORIENTATION_INNER ? getHeight() : getHeight() - mCurveHeight;
            default:
                return 0;
        }
    }

    private float getArcEndY() {
        return mCurveGravity == CURVE_GRAVITY_TOP ? 0 : getHeight();
    }

}
