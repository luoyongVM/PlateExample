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
import android.view.animation.DecelerateInterpolator;


/**
 * 类作用：信用表盘
 * Created ly on 17-7-17.
 */
public class CreditCheckView extends View {
    private static final int BAD = 0;
    private static final int MEDIUM = 1;
    private static final int GOOD = 2;
    private static final int BEST = 3;
    private static final int TIPTOP = 4;

    private boolean isChangeColor;
    private int badColor;
    private int mediumColor;
    private int goodColor;
    private int bestColor;
    private int tiptopColor;
    private int normalColor;
    private int defaultCreditColor;
    private int maxCreditNum;
    private int pointColor;
    private int minWidth = ConvertUtils.dp2px(getContext(),180);
    private Paint paint;
    private TextPaint textPaint;
    private Path textPath;

    private int currentCreditValue = 0;
    private float mean;
    private String evaluateTime = "评估时间:2018-6-21";
    private float pointMeanDegrees;
    private int peripheryLineWidth;
    private int peripheryLineWidth2;
    private int peripheryLineAcrLT;
    private int peripheryLineAcrLT2;
    private int smallScaleWidth;
    private int bigScaleWidth;
    private int smallScaleHeight;
    private int bigScaleHeight;
    private int enSize;
    private int enFontWidth;
    private int cnSize;
    private int cnFontWidth;
    private int creditNumSize;
    private int creditFontWidth;
    private int timeSize;
    private int timeWidth;

    public CreditCheckView(Context context) {
        super(context);
        init();
        initDrawInfo();
    }


    public CreditCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCheckView);
        isChangeColor = typedArray.getBoolean(R.styleable.CreditCheckView_is_change_color, false);
        badColor = typedArray.getColor(R.styleable.CreditCheckView_bad_color, Color.parseColor("#a60909"));
        mediumColor = typedArray.getColor(R.styleable.CreditCheckView_medium_color, Color.parseColor("#cccc00"));
        goodColor = typedArray.getColor(R.styleable.CreditCheckView_good_color, Color.parseColor("#72f872"));
        bestColor = typedArray.getColor(R.styleable.CreditCheckView_best_color, Color.parseColor("#09a609"));
        tiptopColor = typedArray.getColor(R.styleable.CreditCheckView_tiptop_color, Color.parseColor("#00ffff"));
        normalColor = typedArray.getColor(R.styleable.CreditCheckView_normal_color, Color.parseColor("#7d91f0"));
        pointColor = typedArray.getColor(R.styleable.CreditCheckView_pointer_color, Color.parseColor("#7d91f2"));
        maxCreditNum = typedArray.getInteger(R.styleable.CreditCheckView_max_credit_num, 1400);
        typedArray.recycle();
        initDrawInfo();
    }

    private void init() {
        isChangeColor = false;
        badColor = Color.parseColor("#a60909");
        mediumColor = Color.parseColor("#cccc00");
        goodColor = Color.parseColor("#72f872");
        bestColor = Color.parseColor("#09a609");
        tiptopColor = Color.parseColor("#00ffff");
        normalColor = Color.parseColor("#7d91f0");
        pointColor = Color.parseColor("#7d91f2");
        maxCreditNum = 1400;

    }

    private void initDrawInfo() {
        paint = new Paint();
        paint.setColor(normalColor);
        textPaint = new TextPaint();
        textPaint.setColor(normalColor);
        textPaint.setTextSize(ConvertUtils.sp2px(getContext(),10));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPath = new Path();
        if (currentCreditValue > maxCreditNum) {
            throw new IllegalArgumentException("当前信用值大于最大信用值");
        }
        mean = maxCreditNum / 5;
        pointMeanDegrees = 210f / maxCreditNum;  //每个刻度的度数

        //外围线宽度
        peripheryLineWidth = ConvertUtils.dp2px(getContext(),2);
        //外围第二个线宽度
        peripheryLineWidth2 = ConvertUtils.dp2px(getContext(),5);
        //外围线内切正方形位置
        peripheryLineAcrLT = ConvertUtils.dp2px(getContext(),15);
        //外围线2内切正方形位置
        peripheryLineAcrLT2 = ConvertUtils.dp2px(getContext(),60);
        //小刻度线宽度
        smallScaleWidth = ConvertUtils.dp2px(getContext(),2);
        //大刻度线宽度
        bigScaleWidth = ConvertUtils.dp2px(getContext(),3);
        //小刻度线长度
        smallScaleHeight = ConvertUtils.dp2px(getContext(),6);
        //大刻度线长度
        bigScaleHeight = ConvertUtils.dp2px(getContext(),10);
        //英文字体大小
        enSize = ConvertUtils.sp2px(getContext(),12);
        enFontWidth = ConvertUtils.dp2px(getContext(),2);
        //中文字体大小
        cnSize = ConvertUtils.sp2px(getContext(),20);
        cnFontWidth = ConvertUtils.dp2px(getContext(),4);
        // 信用值字体大小
        creditNumSize = ConvertUtils.sp2px(getContext(),60);
        creditFontWidth = ConvertUtils.dp2px(getContext(),6);
        //评估时间字体大小
        timeSize = ConvertUtils.sp2px(getContext(),12);
        timeWidth = ConvertUtils.dp2px(getContext(),3);


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
        drawPeriphery(canvas);
        drawText(canvas);
    }

    /**
     * 绘制内部文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        canvas.restore();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        textPaint.setTextSize(enSize);
        textPaint.setStyle(Paint.Style.FILL);
        if (currentCreditValue >= 0 && currentCreditValue < mean) {
            //bad
            drawCreditGrade(canvas, BAD);
        } else if (currentCreditValue >= mean && currentCreditValue < mean * 2) {
            drawCreditGrade(canvas, MEDIUM);
        } else if (currentCreditValue >= mean * 2 && currentCreditValue < mean * 3) {
            drawCreditGrade(canvas, GOOD);
        } else if (currentCreditValue >= mean * 3 && currentCreditValue < mean * 4) {
            drawCreditGrade(canvas, BEST);
        } else {
            drawCreditGrade(canvas, TIPTOP);
        }
        textPaint.setTextSize(creditNumSize);
        textPaint.setStrokeWidth(creditFontWidth);
        int textWidth = (int) textPaint.measureText(currentCreditValue + "", 0, String.valueOf(currentCreditValue).length());
        canvas.drawText(currentCreditValue + "", (0 - textWidth / 2), -ConvertUtils.dp2px(getContext(),20), textPaint);
        textPaint.setTextSize(timeSize);
        textPaint.setStrokeWidth(timeWidth);
        int dateWidth = (int) textPaint.measureText(evaluateTime, 0, evaluateTime.length());
        canvas.drawText(evaluateTime, (0 - dateWidth / 2), ConvertUtils.dp2px(getContext(),35), textPaint);
    }


    /**
     * 绘制级别文件
     *
     * @param canvas
     * @param grade
     */
    private void drawCreditGrade(Canvas canvas, int grade) {
        int textWidth = 0;
        int contentTextWidth = 0;
        String en_text = "";
        String cn_text = "";
        switch (grade) {
            case BAD:
                en_text = "BAD";
                cn_text = "信用极差";
                break;
            case MEDIUM:
                en_text = "MEDIUM";
                cn_text = "信用中等";
                break;
            case GOOD:
                en_text = "GOOD";
                cn_text = "信用好";
                break;
            case BEST:
                en_text = "BEST";
                cn_text = "信用很好";
                break;
            case TIPTOP:
                en_text = "TIPTOP";
                cn_text = "信用极好";
                break;
        }
        textWidth = (int) textPaint.measureText(en_text, 0, en_text.length());
        canvas.drawText(en_text, (0 - textWidth / 2), -ConvertUtils.dp2px(getContext(),80), textPaint);
        textPaint.setTextSize(cnSize);
        textPaint.setStrokeWidth(cnFontWidth);
        contentTextWidth = (int) textPaint.measureText(cn_text, 0, cn_text.length());
        canvas.drawText(cn_text, 0 - contentTextWidth / 2, ConvertUtils.dp2px(getContext(),20), textPaint);
    }

    /**
     * 绘制外围
     *
     * @param canvas
     */
    private void drawPeriphery(Canvas canvas) {
        canvas.save();
        int width = getWidth();
        int height = getHeight();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(peripheryLineWidth);
        canvas.drawArc(peripheryLineAcrLT, peripheryLineAcrLT, width - peripheryLineAcrLT, width - peripheryLineAcrLT, 165, 210, false, paint);
        paint.setStrokeWidth(peripheryLineWidth2);
        canvas.drawArc(peripheryLineAcrLT2, peripheryLineAcrLT2, width - peripheryLineAcrLT2, width - peripheryLineAcrLT2, 165, 210, false, paint);
        paint.setStrokeWidth(smallScaleWidth);//小刻度线宽度
        canvas.translate(width / 2, height / 2);
        //        canvas.rotate(165);
        canvas.rotate(-15);
        for (int i = 0; i < 29; i++) {
            if (i % 2 == 0) {
                paint.setStrokeWidth(bigScaleWidth);//大刻度线宽度
                canvas.drawLine(-(width / 2 - peripheryLineAcrLT2), 0, -(width / 2 - (peripheryLineAcrLT2-bigScaleHeight)), 0, paint);
                if (i != 0 && i != 28) {
                    textPath.moveTo(-(width / 2 - ConvertUtils.dp2px(getContext(),45)), ConvertUtils.dp2px(getContext(),10));
                    textPath.lineTo(-(width / 2 - ConvertUtils.dp2px(getContext(),45)), - ConvertUtils.dp2px(getContext(),15));
                    canvas.drawTextOnPath(maxCreditNum / 14 * (i / 2) + "", textPath, 0, 0, textPaint);
                    textPath.reset();
                    // canvas.drawText(maxCreditNum / 14 * (i / 2) + "", width / 2 - 65, 0, textPaint);
                }
            } else {
                paint.setStrokeWidth(smallScaleWidth);
                canvas.drawLine(-(width / 2 - peripheryLineAcrLT2), 0, -(width / 2 - (peripheryLineAcrLT2-smallScaleHeight)), 0, paint);
            }
            canvas.rotate(7.5f);
        }
        drawPointer(canvas, currentCreditValue);
    }

    private void drawPointer(Canvas canvas, int currentCreditValue) {
        canvas.restore();
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(currentCreditValue * pointMeanDegrees + (-195));
        canvas.drawCircle(getWidth() / 2 - ConvertUtils.dp2px(getContext(),14), 0, ConvertUtils.dp2px(getContext(),11), textPaint);
    }

    public boolean isChangeColor() {
        return isChangeColor;
    }

    public void setChangeColor(boolean changeColor) {
        isChangeColor = changeColor;
    }

    public int getBadColor() {
        return badColor;
    }

    public void setBadColor(int badColor) {
        this.badColor = badColor;
    }

    public int getMediumColor() {
        return mediumColor;
    }

    public void setMediumColor(int mediumColor) {
        this.mediumColor = mediumColor;
    }

    public int getGoodColor() {
        return goodColor;
    }

    public void setGoodColor(int goodColor) {
        this.goodColor = goodColor;
    }

    public int getBestColor() {
        return bestColor;
    }

    public void setBestColor(int bestColor) {
        this.bestColor = bestColor;
    }

    public int getTiptopColor() {
        return tiptopColor;
    }

    public void setTiptopColor(int tiptopColor) {
        this.tiptopColor = tiptopColor;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getDefaultCreditColor() {
        return defaultCreditColor;
    }

    public void setDefaultCreditColor(int defaultCreditColor) {
        this.defaultCreditColor = defaultCreditColor;
    }

    public int getMaxCreditNum() {
        return maxCreditNum;
    }

    public void setMaxCreditNum(int maxCreditNum) {
        this.maxCreditNum = maxCreditNum;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public void setCurrentCreditValue(int currentCreditValue) {
        this.currentCreditValue = currentCreditValue;
        startAnimation();
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, currentCreditValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                currentCreditValue = value;
                invalidate();
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }
}
