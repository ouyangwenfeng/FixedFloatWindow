package com.example.fixedfloatwindow;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 自定义 toast
 * 4.4~7.0 无需权限可点击
 */

class FixedFloatToast implements FixedFloatView {


    private Toast toast;

    private Object mTN;
    private Method show;
    private Method hide;

    private int mWidth = FixedFloatWindow.WRAP_CONTENT;
    private int mHeight = FixedFloatWindow.WRAP_CONTENT;


    public FixedFloatToast(Context applicationContext) {
        toast = new Toast(applicationContext);
    }


    public void setView(View view, int width, int height) {
        mWidth = width;
        mHeight = height;
        setView(view);
    }


    public void setView(View view) {
        toast.setView(view);
        initTN();
    }


    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }


    public void show() {
        try {
            show.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hide() {
        try {
            hide.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 利用反射设置 toast 参数
     */
    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");

            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.width = mWidth;
            params.height = mHeight;
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
