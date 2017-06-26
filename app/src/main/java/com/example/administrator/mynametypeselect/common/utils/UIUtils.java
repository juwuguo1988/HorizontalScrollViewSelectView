package com.example.administrator.mynametypeselect.common.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.mynametypeselect.common.XZLApplication;
import java.lang.reflect.Field;

/**
 * UI工具类，包括dp，px，sp的转换；获取状态栏高度，弹出Toast...
 */
public class UIUtils {

    public static int px2dp(float pxValue) {
        return (int) (pxValue / XZLApplication.screenDensity + 0.5f);
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * XZLApplication.screenDensity + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / XZLApplication.scaledDensity);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * XZLApplication.scaledDensity);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int stateBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    private static Toast mToast = null;

    /**
     * Create a toast and show it
     *
     * @param context
     * @param msg
     */
    public static void createToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * Create a toast and show it
     *
     * @param context
     * @param strId
     */
    public static void createToast(Context context, int strId) {
        if (context == null) {
            return;
        }
        createToast(context, context.getResources().getString(strId));
    }

    public static GradientDrawable createRoundRect(int color, int corner) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        return drawable;
    }

    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void hideKeyboard(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
