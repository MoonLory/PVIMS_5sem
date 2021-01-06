package com.example.example821;

import androidx.appcompat.app.AppCompatActivity;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    GestureLibrary gLib;
    GestureOverlayView gestures;

    TextView tvOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gLib.load()) {
            finish();
        }

        gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
        gestures.addOnGesturePerformedListener(this);

        tvOut = findViewById(R.id.textView1);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        //Создаёт ArrayList c загруженными из gestures жестами
        ArrayList<Prediction> predictions = gLib.recognize(gesture);

        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);

            if (prediction.score > 1.0) {
                if (prediction.name.equals("one")) {
                    tvOut.setText("1");
                } else if (prediction.name.equals("Stop")) {
                    tvOut.setText("stop");
                } else if (prediction.name.equals("two")) {
                    tvOut.setText("2");
                }
            } else {
                tvOut.setText("Жест неизвестен");
            }
        }
    }
}