package com.epicodus.wiggleroom.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.wiggleroom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    GestureDetector gestureDetector;
    @Bind(R.id.textView) TextView mTextView;
    @Bind(R.id.imageView) ImageView mImageView;
    private GestureDetectorCompat mTextViewDetector;
    private GestureDetectorCompat mImageViewDetector;
    Context mContext;

    private static final String DEBUG_TAG = "Gestures";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mTextViewDetector = new GestureDetectorCompat(this,new TextViewGestureListener());
        mTextView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mTextViewDetector.onTouchEvent(event);
                return true;
            }
        });
        mImageViewDetector = new GestureDetectorCompat(this, new ImageViewGestureListener());
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mImageViewDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    class TextViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            mTextView.setTextColor(Color.RED);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            mTextView.setTextColor(Color.BLACK);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            Animation doubleTapAnimation = AnimationUtils.loadAnimation(mContext, R.anim.double_tap_animation);
            mTextView.startAnimation(doubleTapAnimation);
            return true;
        }
    }

    class ImageViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "ImageGestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Animation flingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.boomerang_fling_animation);
            mImageView.startAnimation(flingAnimation);
//            mImageView.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500);
//            mImageView.animate().scaleX(1f).scaleY(1f).setDuration(500);

            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }


}
