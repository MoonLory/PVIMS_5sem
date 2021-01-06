package com.example.guess_the_number_gestures;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private final int BOUND = 200;

    TextView tvInfo;
    EditText etInput;
    Button bControl;

    int randomNumber;
    boolean gameIsEnded;

    GestureLibrary gLib;
    GestureOverlayView gestures;

    private boolean checkRange(int start, int end, int number) {
        return (number >= start) && (number <= end);
    }

    public void onClickInfoButton(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.info_button));
        builder.setMessage(getResources().getString(R.string.info_author));

        // add a button
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClick(View v) {
        if (!gameIsEnded) {
            try {
                int input_number = Integer.parseInt(etInput.getText().toString());

                if (!checkRange(0, BOUND, input_number)) {
                    tvInfo.setText(getResources().getString(R.string.error));
                    tvInfo.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvInfo.setTextColor(getResources().getColor(R.color.black)); // Back to standard text color

                    if (input_number > randomNumber) {
                        tvInfo.setText(getResources().getString(R.string.ahead));
                    } else if (input_number < randomNumber) {
                        tvInfo.setText(getResources().getString(R.string.behind));
                    } else {
                        tvInfo.setText(getResources().getString(R.string.hit));
                        bControl.setText(getResources().getString(R.string.play_more));
                        bControl.setTextColor(getResources().getColor(R.color.turquoise));
                        gameIsEnded = true;
                    }
                }
            } catch (IllegalArgumentException e) {
                tvInfo.setText(getResources().getString(R.string.error));
            }
        }

        else {
            randomNumber = ThreadLocalRandom.current().nextInt(0, BOUND + 1);
            bControl.setText(getResources().getString(R.string.input_value));
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            gameIsEnded = false;
        }

        etInput.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.textView);
        etInput = findViewById(R.id.editTextNumber);
        bControl = findViewById(R.id.button);

        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
        gestures.addOnGesturePerformedListener(this);

        randomNumber = ThreadLocalRandom.current().nextInt(0, BOUND + 1);
        gameIsEnded = false;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gLib.recognize(gesture);

        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals("zero")) {
                    etInput.setText(etInput.getText().append("0"));
                } else if (prediction.name.equals("one")) {
                    etInput.setText(etInput.getText().append("1"));
                } else if (prediction.name.equals("two")) {
                    etInput.setText(etInput.getText().append("2"));
                } else if (prediction.name.equals("three")) {
                    etInput.setText(etInput.getText().append("3"));
                } else if (prediction.name.equals("four")) {
                    etInput.setText(etInput.getText().append("4"));
                } else if (prediction.name.equals("five")) {
                    etInput.setText(etInput.getText().append("5"));
                } else if (prediction.name.equals("six")) {
                    etInput.setText(etInput.getText().append("6"));
                } else if (prediction.name.equals("seven")) {
                    etInput.setText(etInput.getText().append("7"));
                } else if (prediction.name.equals("eight")) {
                    etInput.setText(etInput.getText().append("8"));
                } else if (prediction.name.equals("nine")) {
                    etInput.setText(etInput.getText().append("9"));
                } else if (prediction.name.equals("finish")) {
                    onClick(etInput);
                }
            }
        }
    }


}