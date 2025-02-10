package com.example.TicTacToe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class WinAnimationView extends View {
    private Drawable winAnimation;
    private Animatable animatable;

    public WinAnimationView(Context context) {
        super(context);
        init();
    }

    public WinAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WinAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0x00000000); // Transparent background
    }

    public void setWinAnimation(Drawable drawable) {
        this.winAnimation = drawable;
        if (drawable instanceof Animatable) {
            this.animatable = (Animatable) drawable;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (winAnimation != null) {
            winAnimation.setBounds(0, 0, getWidth(), getHeight());
            winAnimation.draw(canvas);
            if (animatable != null) {
                animatable.start();
            }
        }
    }
}