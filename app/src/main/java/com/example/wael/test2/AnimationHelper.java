package com.example.wael.test2;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewAnimationUtils;

public class AnimationHelper {

    public static void startRevealAnimation(@NonNull View layout1, @NonNull View layout2) {
        int x = layout1.getRight();
        int y = layout1.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layout1.getWidth(), layout1.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(layout2, x, y, startRadius, endRadius);
            layout2.setVisibility(View.VISIBLE);
            animator.start();
        }
    }

    public static void endRevealAnimation(@NonNull View layout1, @NonNull View layout2) {
        int x = layout1.getRight();
        int y = layout1.getBottom();

        int startRadius = (int) Math.hypot(layout1.getWidth(), layout1.getHeight());
        int endRadius = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(layout2, x, y, startRadius, endRadius);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layout2.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animator.start();
        }
    }
}
