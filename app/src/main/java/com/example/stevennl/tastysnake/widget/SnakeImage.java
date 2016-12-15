package com.example.stevennl.tastysnake.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;

/**
 * A snake image with animation.
 * Author: LCY
 */
public class SnakeImage extends ImageView {
    private static final String TAG = "SnakeImage";
    private static final String ATTR_TRANSY = "translationY";

    private Direction direc;
    private int blinkImgId;
    private int pressedImgId;
    private int offsetMarginPixel;

    private ObjectAnimator anim;
    private View.OnClickListener onClickListener;

    /**
     * Initialize from code.
     *
     * @param context The context
     */
    public SnakeImage(Context context) {
        this(context, null);
    }

    /**
     * Initialize from XML resources file.
     *
     * @param context The context
     * @param attrs The attributes set
     */
    public SnakeImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Constructor called.");
        if (attrs != null) {
            initCustomAttr(context, attrs);
        }
        startBlinkAnim();
    }

    /**
     * Initialize custom attributes.
     */
    private void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SnakeImageAttr);
        blinkImgId = arr.getResourceId(R.styleable.SnakeImageAttr_blinkImage, -1);
        pressedImgId = arr.getResourceId(R.styleable.SnakeImageAttr_pressedImage, -1);
        offsetMarginPixel = arr.getDimensionPixelSize(R.styleable.SnakeImageAttr_offsetMargin, -1);
        offsetMarginPixel = Math.abs(offsetMarginPixel);
        int direcIndex = arr.getInteger(R.styleable.SnakeImageAttr_direction, Direction.NONE.ordinal());
        direc = Direction.values()[direcIndex];
        arr.recycle();
    }

    /**
     * Start eye blinking animation.
     */
    public void startBlinkAnim() {
        setBackgroundResource(blinkImgId);
        ((AnimationDrawable)getBackground()).start();
    }

    /**
     * Show snake enter animation.
     *
     * @param endListener An {@link AnimationEndListener}
     */
    public void startEnter(@Nullable final AnimationEndListener endListener) {
        float dist = offsetMarginPixel;
        switch (direc) {
            case UP:
                anim = ObjectAnimator.ofFloat(this, ATTR_TRANSY, 0, -dist)
                        .setDuration(Config.DURATION_SNAKE_ANIM);
                break;
            case DOWN:
                anim = ObjectAnimator.ofFloat(this, ATTR_TRANSY, 0, dist)
                        .setDuration(Config.DURATION_SNAKE_ANIM);
                break;
            default:
                break;
        }
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (endListener != null) {
                    endListener.onAnimationEnd(animation);
                }
                anim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Do nothing
            }
        });
        anim.start();
    }

    /**
     * Show snake exit animation.
     *
     * @param endListener An {@link AnimationEndListener}
     */
    public void startExit(@Nullable final AnimationEndListener endListener) {
        float offset = getHeight() - offsetMarginPixel;
        switch (direc) {
            case UP:
                anim = ObjectAnimator.ofFloat(this, ATTR_TRANSY, getTranslationY(), offset)
                        .setDuration(Config.DURATION_SNAKE_ANIM);
                break;
            case DOWN:
                anim = ObjectAnimator.ofFloat(this, ATTR_TRANSY, getTranslationY(), -offset)
                        .setDuration(Config.DURATION_SNAKE_ANIM);
                break;
            default:
                break;
        }
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (endListener != null) {
                    endListener.onAnimationEnd(animation);
                }
                anim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Do nothing
            }
        });
        anim.start();
    }

    /**
     * Cancel animations.
     */
    public void cancelAnim() {
        if (anim != null) {
            anim.cancel();
        }
    }

    /**
     * Set an {@link OnClickListener}.
     */
    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(pressedImgId);
                break;
            case MotionEvent.ACTION_UP:
                startBlinkAnim();
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * A listener called when the animation is finished.
     */
    public interface AnimationEndListener {
        void onAnimationEnd(Animator animation);
    }
}
