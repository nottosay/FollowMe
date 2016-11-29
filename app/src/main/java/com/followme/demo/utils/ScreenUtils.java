package com.followme.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * Created by wally.yan on 2016/11/23.
 */

public class ScreenUtils {

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics outMetrics = resources.getDisplayMetrics();
        int width = outMetrics.widthPixels;
        return width;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics outMetrics = resources.getDisplayMetrics();
        int height = outMetrics.heightPixels;
        return height;
    }

    //获取屏幕的高度*宽
    public static String getScreenSize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics outMetrics = resources.getDisplayMetrics();
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width + "*" + height;
    }

    //获取状态栏的高度
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        //使用反射，可能会出现类找不到的异常ClassNotFoundException
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String status_bar_height = clazz.getField("status_bar_height").get(object).toString();
            int height = Integer.parseInt(status_bar_height);
            //转化成px返回
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    //获取当前屏幕截图，包括状态栏
    public static Bitmap getSnapshot(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int screenWidth = getScreenWidth(activity);
        int screenHeight = getScreenHeight(activity);
        Bitmap bp;
        bp = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static int sp2px(Context context, int i) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float fontScale = displayMetrics.scaledDensity;
        return (int) (i * fontScale + 0.5f);
    }


    //dp转化成px   其他的模仿就行了  因为1dp=1sp的
    public static int dp2px(Context context, float dpValue) {
        Resources resources = context.getResources();
        //用于存储显示的通用信息，如显示大小，分辨率和字体。
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        //获取转化新系数（这里不太懂 要好好搞一下）
        float scale = displayMetrics.density;
        //在160dpi里面
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        Resources resources = context.getResources();
        //用于存储显示的通用信息，如显示大小，分辨率和字体。
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        //获取转化新系数（这里不太懂 要好好搞一下）
        float scale = displayMetrics.density;
        //在160dpi里面
        return (int) (pxValue / scale + 0.5f);
    }
}
