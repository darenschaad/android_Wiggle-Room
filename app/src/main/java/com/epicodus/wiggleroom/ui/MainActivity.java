package com.epicodus.wiggleroom.ui;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    GestureDetector gestureDetector;
    @Bind(R.id.textView) TextView mTextView;
    @Bind(R.id.imageView) ImageView mImageView;
    @Bind(R.id.imageView2) ImageView mBallImageView;
    @Bind(R.id.bBallImageView) ImageView mBBallImageView;
    private GestureDetectorCompat mTextViewDetector;
    private GestureDetectorCompat mImageViewDetector;
    private GestureDetectorCompat mbBallImageViewDetector;
    Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;
    private static final String DEBUG_TAG = "Gestures";
    public Animation doubleTapAnimation;
    public Animation flingAnimation;
    public Animation longClickAnimation;
    public Animation swipeAnimation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        doubleTapAnimation = AnimationUtils.loadAnimation(mContext, R.anim.double_tap_animation);
        flingAnimation =AnimationUtils.loadAnimation(mContext, R.anim.boomerang_fling_animation);
        longClickAnimation = AnimationUtils.loadAnimation(mContext, R.anim.ball_animation);
        swipeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.basket_ball_animation);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);
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
        mBallImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mBallImageView.startAnimation(longClickAnimation);
                return false;

            }
        });
        mbBallImageViewDetector = new GestureDetectorCompat(this, new BBallImageViewGestureListener());
        mBBallImageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mbBallImageViewDetector.onTouchEvent(event);
                return true;
            }
        });
//        mImageView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                mBallImageViewDetector.onTouchEvent(event);
//                return true;
//            }
//        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100 ) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("SensorEventListener", "shaking");
                    mTextView.startAnimation(doubleTapAnimation);
                    mImageView.startAnimation(flingAnimation);
                    mBallImageView.startAnimation(longClickAnimation);
                    mBBallImageView.startAnimation(swipeAnimation); 


                    last_x = x;
                    last_y = y;
                    last_z = z;

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
            mTextView.startAnimation(doubleTapAnimation);
            return true;
        }
    }

    class ImageViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "ImageGestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
//            Animation flingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.boomerang_fling_animation);
            mImageView.startAnimation(flingAnimation);
//            mImageView.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500);
//            mImageView.animate().scaleX(1f).scaleY(1f).setDuration(500);

            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }

    class BBallImageViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "BBallImageGestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
//            Animation flingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.boomerang_fling_animation);
            mBBallImageView.startAnimation(swipeAnimation);
//            mImageView.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500);
//            mImageView.animate().scaleX(1f).scaleY(1f).setDuration(500);

            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }


}
