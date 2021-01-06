package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    private TextView tvOut;
    private ImageView AirImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AirImg = findViewById(R.id.AirImg);

        mDetector = new GestureDetectorCompat(this, new MyGestListener());
        tvOut = (TextView)findViewById(R.id.textView1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            tvOut.setText("onFling");
            AirImg.setImageResource(R.drawable.hack1);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            tvOut.setText("OnSignleTapUp");
            AirImg.setImageResource(R.drawable.hack);

            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            tvOut.setText("onDoubleTapUp");
            AirImg.setImageResource(R.drawable.hack2);

            return true;
        }
    }
}