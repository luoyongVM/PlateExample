package com.lygit.plate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 类作用：雷达表盘
 * Created ly on 17-7-17.
 */
public class SpiderChartView extends View {
    private int maxValue;
    private int bgLineColor;
    private int num; //角的个数
    private int minWidth = ConvertUtils.dp2px(getContext(),180);
    private float everyAngle; //每个角的个数
    private Paint paint;
    private int[] values;
    private float scaleSize;
    private int pointSize;
    private int padding;
    private int realWidth;
    private Paint textPaint;
    private String[]valuesText;
    private Path pointPath;
    private Path bgPath;

    public SpiderChartView(Context context) {
        super(context);
        init();
    }

    public SpiderChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.spider_chart);
        num = typedArray.getInteger(R.styleable.spider_chart_num, 6);
        bgLineColor = typedArray.getColor(R.styleable.spider_chart_bg_line_color, Color.parseColor("#dbdfde"));
        maxValue = typedArray.getInteger(R.styleable.spider_chart_max_value, 100);
        typedArray.recycle();
        initData();
    }

    private void init() {
        num = 6;
        initData();
    }

    private void initData() {
        everyAngle = 360f / num;
        paint = new Paint();
        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#06979a"));
        textPaint.setTextSize(ConvertUtils.sp2px(getContext(),12));
        values = new int[num];
        values[0] = 0;
        values[1] = 0;
        values[2] = 0;
        values[3] = 0;
        values[4] = 0;
        values[5] = 0;
        valuesText = new String[6];

        valuesText[0] = "身份";
        valuesText[1] = "交易";
        valuesText[2] = "圈子";
        valuesText[3] = "历史";
        valuesText[4] = "行为";
        valuesText[5] = "履约";
        pointSize = ConvertUtils.dp2px(getContext(),4);
        padding = ConvertUtils.dp2px(getContext(),100);

        pointPath = new Path();
        bgPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int resultWidth = 0;
        int resultHeight = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                if (width < minWidth) {
                    resultWidth = minWidth;
                    resultHeight = resultWidth;
                } else {
                    resultWidth = width;
                    resultHeight = width;
                }
                break;
            case MeasureSpec.EXACTLY:
                if (width < minWidth) {
                    resultWidth = minWidth;
                    resultHeight = resultWidth;
                } else {
                    resultWidth = width;
                    resultHeight = resultWidth;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                resultWidth = minWidth;
                resultHeight = resultWidth;
                break;
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        realWidth = getWidth() - padding;
        scaleSize = (realWidth/ 2f) / maxValue;
        drawBg(canvas);
        drawPoint(canvas);
        super.onDraw(canvas);
    }


    /**
     * 画bg
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        bgPath.reset();
        canvas.save();
        paint.setColor(bgLineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ConvertUtils.dp2px(getContext(),3));
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float interval = realWidth / 5;
        for (int i = 0; i < 5; i++) {
            float height = realWidth - i * interval;
            bgPath.moveTo(0, -height / 2);
            bgPath.lineTo((float) (Math.sin(Math.PI / 180 * 60) * (height / 2f)), -height / 2 / 2);
            bgPath.lineTo((float) (Math.sin(Math.PI / 180 * 60) * (height / 2f)), height / 2 / 2);
            bgPath.lineTo(0, height / 2);
            bgPath.lineTo(-(float) (Math.sin(Math.PI / 180 * 60) * (height / 2f)), height / 2 / 2);
            bgPath.lineTo(-(float) (Math.sin(Math.PI / 180 * 60) * (height / 2f)), -height / 2 / 2);
            bgPath.lineTo(0, -height / 2);
            if (i % 2 == 0) {
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(bgPath, paint);

                paint.setColor(Color.parseColor("#bbbbbb"));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(ConvertUtils.dp2px(getContext(),1));
                canvas.drawPath(bgPath, paint);

                bgPath.reset();
            } else {
                paint.setColor(Color.parseColor("#efefef"));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(bgPath, paint);

                paint.setColor(Color.parseColor("#bbbbbb"));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(ConvertUtils.dp2px(getContext(),1));
                canvas.drawPath(bgPath, paint);

                bgPath.reset();
            }
        }
        for (int i = 0; i < num; i++) {
            canvas.drawText(valuesText[i], -20, -realWidth/2-ConvertUtils.dp2px(getContext(),10),textPaint);
            canvas.drawLine(0, -realWidth / 2, 0, realWidth / 2, paint);
            canvas.rotate(everyAngle);
        }
    }


    /**
     * 画点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        pointPath.reset();
        canvas.restore();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        paint.setColor(Color.RED);
        float r = values[0] * scaleSize;
        pointPath.moveTo(0, -r);
        pointPath.addCircle(0, -r, pointSize, Path.Direction.CW);
        for (int i = 1; i < num; i++) {
            r = values[i] * scaleSize;
            float x = (float) Math.cos(Math.PI / 180 * (-30 + (60 * (i - 1)))) * r;
            float y = (float) Math.sin(Math.PI / 180 * (-30 + (60 * (i - 1)))) * r;
            pointPath.lineTo(x, y);
            pointPath.addCircle(x, y, pointSize, Path.Direction.CW);
            pointPath.moveTo(x,y);
        }
        r = values[0] * scaleSize;
        pointPath.lineTo(0, -r);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(ConvertUtils.dp2px(getContext(),2));
        canvas.drawPath(pointPath, paint);

    }




    public void setValues(int v[]) {
        for (int i=0;i<v.length;i++) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, v[i]);
            final int finalI = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    values[finalI] = v;
                    invalidate();
                }
            });
            valueAnimator.setDuration(3000);
            valueAnimator.start();
        }
    }
}
