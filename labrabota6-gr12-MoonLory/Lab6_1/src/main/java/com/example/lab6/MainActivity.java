package com.example.Lab6_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvInfo;
    EditText etInput;
    Button bControl;

    int maxNumber = 200;
    int minNumber = 1;

    int guess;
    boolean gameFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale myLocale = new Locale("ru");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        tvInfo = (TextView) findViewById(R.id.textView2);
        etInput = (EditText) findViewById(R.id.editTextNumber);
        bControl = (Button) findViewById(R.id.button);

        guess = (int) (Math.random() * 100);
        gameFinished = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        if (!gameFinished) {
            int inp;

            try {
                inp = Integer.parseInt(etInput.getText().toString());
            }catch (Exception e){
                tvInfo.setText((getResources().getString(R.string.invalidFormat)));
                etInput.setText("");
                return;
            }

            if(inp < minNumber || inp > maxNumber){
                tvInfo.setText((getResources().getString(R.string.outOfBorder)
                        + "(" + minNumber + ", " + maxNumber + ")"));
                etInput.setText("");
                return;
            }
            if (inp > guess)
                tvInfo.setText(getResources().getString(R.string.ahead));
            if (inp < guess)
                tvInfo.setText(getResources().getString(R.string.behind));
            if (inp == guess) {
                tvInfo.setText(getResources().getString(R.string.hit));
                bControl.setText(getResources().getString(R.string.play_more));
                gameFinished = true;
            }
        } else {
            guess = (int) (Math.random() * (maxNumber-minNumber) + minNumber);
            bControl.setText(getResources().getString(R.string.input_value));
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            gameFinished = false;
        }
        etInput.setText("");
    }
}
