package com.example.clockproject;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class timeNow extends Fragment {

    //현재 시각 구하기
    long now = System.currentTimeMillis();

    //현재 시간을 변수에 저장
    Date time = new Date(now);

    //시간을 나타낼 포맷 정함
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //nowDate 변수에 값 저장
    String formatDate = sdfNow.format(time);

    TextView timeNow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public timeNow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.timenow_fragment, container, false);
        timeNow = (TextView) view.findViewById(R.id.timeNowText);
        ShowTimeMethod();
        //timeNow.setText(formatDate);

        return view;
    }

    public void ShowTimeMethod() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                timeNow.setText(DateFormat.getDateTimeInstance().format(new Date()));
            }
        };
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e) {}
                    handler.sendEmptyMessage(1);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
