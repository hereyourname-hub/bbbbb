package com.example.bodyboost;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import android.os.Handler;

import com.example.bodyboost.R;

public class ThirdActivity2 extends AppCompatActivity {

    private String buttonvalue;
    private Button startBtn;
    private CountDownTimer countDownTimer;
    private TextView mtextview;
    private boolean MTimeRunning;
    private long MTimeLeftinmills;
    private TextToSpeech textToSpeech;
    private Handler handler = new Handler();
    private boolean timerFlashing = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);

        Intent intent = getIntent();
        buttonvalue = intent.getStringExtra("value");

        int intvalue = Integer.valueOf(buttonvalue);

        switch (intvalue) {
            case 1:
                setContentView(R.layout.activity_bow2);
                break;
            case 2:
                setContentView(R.layout.activity_bridge2);
                break;
            case 3:
                setContentView(R.layout.activity_chair2);
                break;
            case 4:
                setContentView(R.layout.activity_child2);
                break;
            case 5:
                setContentView(R.layout.activity_cobbler2);
                break;
            case 6:
                setContentView(R.layout.activity_cow2);
                break;
            case 7:
                setContentView(R.layout.activity_playji2);
                break;
            case 8:
                setContentView(R.layout.activity_pauseji2);
                break;
            case 9:
                setContentView(R.layout.activity_plank2);
                break;
            case 10:
                setContentView(R.layout.activity_crunches2);
                break;
        }

        startBtn = findViewById(R.id.startbutton);
        mtextview = findViewById(R.id.time);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        textToSpeech = null;
                    }
                } else {
                    textToSpeech = null;
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MTimeRunning) {
                    stopTimer();
                } else {
                    startCountdown();
                }
            }
        });
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        MTimeRunning = false;
        startBtn.setText("START");
        stopTimerFlashing(); // Stop timer flashing if it's running
        resetTimerTextColor(); // Reset timer text color to default
    }

    private void resetTimerTextColor() {
        TextView timerTextView = findViewById(R.id.time);
        timerTextView.setTextColor(Color.BLACK);
    }

    private void startCountdown() {
        startBtn.setText("1");
        if (textToSpeech != null) {
            textToSpeech.speak("1", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startBtn.setText("2");
                if (textToSpeech != null) {
                    textToSpeech.speak("2", TextToSpeech.QUEUE_FLUSH, null, null);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startBtn.setText("3");
                        if (textToSpeech != null) {
                            textToSpeech.speak("3", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startBtn.setText("GO");
                                if (textToSpeech != null) {
                                    textToSpeech.speak("Go", TextToSpeech.QUEUE_FLUSH, null, null);
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startBtn.setText("Pause");
                                        startExerciseTimer();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        }, 1000);
    }

    private void startExerciseTimer() {
        final CharSequence value1 = mtextview.getText();
        String num1 = value1.toString();
        String num2 = num1.substring(0, 2);
        String num3 = num1.substring(3, 5);

        final int number = Integer.valueOf(num2) * 60 + Integer.valueOf(num3);
        MTimeLeftinmills = number * 1000;

        countDownTimer = new CountDownTimer(MTimeLeftinmills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MTimeLeftinmills = millisUntilFinished;
                updateTimer();

                // Voice warning with 5 seconds remaining
                if (millisUntilFinished <= 5000) {
                    if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                        textToSpeech.speak("5 seconds left", TextToSpeech.QUEUE_FLUSH, null, null);
                        startTimerFlashing(); // Start flashing the timer text
                    }
                }
            }

            @Override
            public void onFinish() {
                MTimeRunning = false;
                startBtn.setText("Pause");
                stopTimerFlashing(); // Stop timer flashing before proceeding to next exercise
                pauseBeforeRestTimer();
            }
        }.start();

        MTimeRunning = true;
        startBtn.setText("Pause");
    }

    private void pauseBeforeRestTimer() {
        // Pause for 5 seconds
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Voice warning with 5 seconds remaining
                if (millisUntilFinished <= 5000) {
                    if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                        textToSpeech.speak("Get ready for rest", TextToSpeech.QUEUE_FLUSH, null, null);
                        startTimerFlashing(); // Start flashing the timer text
                    }
                }
            }

            @Override
            public void onFinish() {
                stopTimerFlashing(); // Stop flashing before starting the rest timer
                resetTimerTextColor(); // Reset timer text color to default
                startRestTimer();
            }
        }.start();
    }

    private void startRestTimer() {
        MTimeLeftinmills = 30000; // 30 seconds rest time

        countDownTimer = new CountDownTimer(MTimeLeftinmills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MTimeLeftinmills = millisUntilFinished;
                updateTimer();

                // Voice warning with 5 seconds remaining
                if (millisUntilFinished <= 5000) {
                    if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                        textToSpeech.speak("5 seconds left", TextToSpeech.QUEUE_FLUSH, null, null);
                        startTimerFlashing(); // Start flashing the timer text
                    }
                }
            }

            @Override
            public void onFinish() {
                MTimeRunning = false;
                startBtn.setText("Pause");
                stopTimerFlashing(); // Stop timer flashing before proceeding to next exercise
                goToNextExercise();
            }
        }.start();

        MTimeRunning = true;
        startBtn.setText("Pause");
    }

    private void goToNextExercise() {
        int newvalue = Integer.valueOf(buttonvalue) + 1;
        if (newvalue <= 10) {
            Intent intent = new Intent(ThirdActivity2.this, ThirdActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("value", String.valueOf(newvalue));
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ThirdActivity2.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void updateTimer() {
        int minutes = (int) MTimeLeftinmills / 60000;
        int seconds = (int) MTimeLeftinmills % 60000 / 1000;

        String timeLeftText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mtextview.setText(timeLeftText);
    }

    private void startTimerFlashing() {
        timerFlashing = true;
        flashingTextColor();
    }

    private void flashingTextColor() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timerFlashing) {
                    TextView timerTextView = findViewById(R.id.time);
                    if (timerTextView.getCurrentTextColor() == Color.RED) {
                        timerTextView.setTextColor(Color.BLACK);
                    } else {
                        timerTextView.setTextColor(Color.RED);
                    }
                    flashingTextColor();
                } else {
                    resetTimerTextColor(); // Reset the timer text color to default
                }
            }
        }, 500); // Change the flashing interval here (in milliseconds)
    }

    private void stopTimerFlashing() {
        timerFlashing = false;
        resetTimerTextColor(); // Reset the timer text color to default
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        stopTimerFlashing(); // Ensure flashing is stopped when the activity is destroyed
    }
}
