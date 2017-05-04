package com.example.pyj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * 启动页
 */

public class SplashFragment extends Fragment {
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);
        imageView = (ImageView) view.findViewById(R.id.SplashImage);
        animateImage(5000);
        return view;
    }

    /**
     * 启动页动画
     */
    private void animateImage(int time) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1f, 1.13F);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1f, 1.13F);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(time).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }
}
