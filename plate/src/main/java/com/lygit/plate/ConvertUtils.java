package com.lygit.plate;

import android.content.Context;

/**
 * 类作用：dp与px之间的转换
 * Created ly on 17-7-17.
 */
public class ConvertUtils {


    /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return px 值
     */
    public static int dp2px(Context context,final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp 转 px
     * @param spValue sp 值
     * @return px 值
     */
    public static int sp2px(Context context,final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
