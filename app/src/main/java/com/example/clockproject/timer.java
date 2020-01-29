package com.example.clockproject;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class timer extends Fragment {

    private EditText mEditTextInput;

    private Button mButtonSet;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftMilliseconds; //10 mins
    private long mEndTime;

    public timer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity ab = getActivity();
        View view = inflater.inflate(R.layout.timer_fragment, container, false);

        mEditTextInput = view.findViewById(R.id.edit_text_input);
        mButtonSet = view.findViewById(R.id.button_set);

        mTextViewCountDown = view.findViewById(R.id.text_view_countdown);

        mButtonStartPause = view.findViewById(R.id.button_start_pause);
        mButtonReset = view.findViewById(R.id.button_reset);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEditTextInput.getText().toString();

                if(input.length() == 0) {
                    Toast.makeText(ab,"Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput < 0) {
                    Toast.makeText(ab, "Please enter positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                }
                else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        return view;
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftMilliseconds;

        mCountDownTimer = new CountDownTimer(mTimeLeftMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFInished) {
                mTimeLeftMilliseconds = millisUntilFInished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftMilliseconds / 1000) / 3600;
        int minutes = (int) ((mTimeLeftMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftMilliseconds / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, seconds);

        }
        else {
            timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void closeKeyboard() {
        MainActivity activity = (MainActivity) getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity activity = (MainActivity) getActivity();
        View view = activity.getCurrentFocus();

        SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftMilliseconds);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) getActivity();
        View view = activity.getCurrentFocus();

        SharedPreferences prefs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeMillis", 0);
        mTimeLeftMilliseconds = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", mTimerRunning);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftMilliseconds = mEndTime - System.currentTimeMillis();

            if (mTimeLeftMilliseconds < 0) {
                mTimeLeftMilliseconds = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            }
            else {
                startTimer();
            }
        }
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("일시정지");
        }
        else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("시작");

            if(mTimeLeftMilliseconds < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            }
            else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftMilliseconds < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            }
            else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftMilliseconds = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

}
