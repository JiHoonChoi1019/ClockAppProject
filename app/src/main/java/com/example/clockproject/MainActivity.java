package com.example.clockproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;
    private final int FRAGMENT4 = 4;

    private Button timeNowBtn, swBtn, timerBtn, alarmBtn;

    timeNow timeNowFragment;
    stopWatch stopWatchFragment;
    timer timerFragment;
    alarm alarmFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯에 대한 참조
        timeNowBtn = (Button)findViewById(R.id.timeNowBtn);
        swBtn = (Button)findViewById(R.id.swBtn);
        timerBtn = (Button)findViewById(R.id.timerBtn);
        alarmBtn = (Button)findViewById(R.id.alarmBtn);

        // 탭 버튼에 대한 리스너 연결
        timeNowBtn.setOnClickListener(this);
        swBtn.setOnClickListener(this);
        timerBtn.setOnClickListener(this);
        alarmBtn.setOnClickListener(this);

        timeNowFragment = new timeNow();
        stopWatchFragment = new stopWatch();
        timerFragment = new timer();
        alarmFragment = new alarm();

        // 첫화면 설정
        callFragment(FRAGMENT1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timeNowBtn :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.swBtn :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;

            case R.id.timerBtn :
                callFragment(FRAGMENT3);
                break;

            case R.id.alarmBtn :
                callFragment(FRAGMENT4);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                transaction.replace(R.id.frameLayout, timeNowFragment);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                transaction.replace(R.id.frameLayout, stopWatchFragment);
                transaction.commit();
                break;

            case 3:
                transaction.replace(R.id.frameLayout, timerFragment);
                transaction.commit();
                break;

            case 4:
                transaction.replace(R.id.frameLayout, alarmFragment);
                transaction.commit();
                break;
        }

    }
}
