package com.csite.app.CustomLayouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorRes;

// Slider Tab layout for active and completed projects

public class SliderTabLayout extends View {

    public static final int ACTIVE_TAB = 0;
    public static final int COMPLETED_TAB = 1;
    private Paint textPaint;
    private static final int TEXT_SIZE = 16;

    private Paint backgroundPaint;
    private Paint selectedTabPaint;
    private RectF activeTabRect;
    private RectF completedTabRect;
    private int selectedTab = ACTIVE_TAB;
    private static final int PADDING = 10; // 3dp padding

    public SliderTabLayout(Context context) {
        super(context);
        init();
    }

    public SliderTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SliderTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.rgb(245, 240, 229));
        backgroundPaint.setStyle(Paint.Style.FILL);

        selectedTabPaint = new Paint();
        selectedTabPaint.setColor(Color.WHITE);
        selectedTabPaint.setStyle(Paint.Style.FILL);

        activeTabRect = new RectF(PADDING, PADDING, 0, 0); // Initialize with padding
        completedTabRect = new RectF(0, PADDING, 0, 0);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK); // Or any color you prefer
        textPaint.setTextSize(TEXT_SIZE * getResources().getDisplayMetrics().density);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int halfWidth = (w - 2 * PADDING) / 2; // Account for padding
        activeTabRect.set(PADDING, PADDING, halfWidth + PADDING, h - PADDING);
        completedTabRect.set(halfWidth + PADDING, PADDING, w - PADDING, h - PADDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cornerRadius = (getHeight() - 2 * PADDING) / 2f; // Oval radius with padding
        RectF backgroundRect = new RectF(0, 0, getWidth(), getHeight());

        // Draw yellow background (now oval)
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);

        // Draw selected tab in white (oval)
        if (selectedTab == ACTIVE_TAB) {
            canvas.drawRoundRect(activeTabRect, cornerRadius, cornerRadius, selectedTabPaint);
        } else {
            canvas.drawRoundRect(completedTabRect, cornerRadius, cornerRadius, selectedTabPaint);
        }
        float textY = getHeight() / 2f + textPaint.getTextSize() / 3f; // Vertical center
        canvas.drawText("Active", activeTabRect.centerX(), textY, textPaint);
        canvas.drawText("Completed", completedTabRect.centerX(), textY, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            if (x < getWidth() / 2) {
                selectTab(ACTIVE_TAB);
            } else {
                selectTab(COMPLETED_TAB);
            }
        }
        return true;
    }

    private void selectTab(int tab) {
        selectedTab = tab;
        notifyTabSelected(tab);
        invalidate(); // Redraw the view

    }



    public interface OnTabSelectedListener {
        void onTabSelected(int tab);
    }

    private OnTabSelectedListener tabSelectedListener;

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.tabSelectedListener = listener;
    }

    private void notifyTabSelected(int tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tab);
        }
    }

}