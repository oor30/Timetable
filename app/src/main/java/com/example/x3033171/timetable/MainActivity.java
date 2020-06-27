package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Lecture[][] lectures;
    LecInfoView lecInfoView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 講義情報を保存・表示するView "Lecture"
        lectures = new Lecture[5][5];
        lectures[0][0] = findViewById(R.id.lec11);
        lectures[0][1] = findViewById(R.id.lec12);
        lectures[0][2] = findViewById(R.id.lec13);
        lectures[0][3] = findViewById(R.id.lec14);
        lectures[0][4] = findViewById(R.id.lec15);
        lectures[1][0] = findViewById(R.id.lec21);
        lectures[1][1] = findViewById(R.id.lec22);
        lectures[1][2] = findViewById(R.id.lec23);
        lectures[1][3] = findViewById(R.id.lec24);
        lectures[1][4] = findViewById(R.id.lec25);
        lectures[2][0] = findViewById(R.id.lec31);
        lectures[2][1] = findViewById(R.id.lec32);
        lectures[2][2] = findViewById(R.id.lec33);
        lectures[2][3] = findViewById(R.id.lec34);
        lectures[2][4] = findViewById(R.id.lec35);
        lectures[3][0] = findViewById(R.id.lec41);
        lectures[3][1] = findViewById(R.id.lec42);
        lectures[3][2] = findViewById(R.id.lec43);
        lectures[3][3] = findViewById(R.id.lec44);
        lectures[3][4] = findViewById(R.id.lec45);
        lectures[4][0] = findViewById(R.id.lec51);
        lectures[4][1] = findViewById(R.id.lec52);
        lectures[4][2] = findViewById(R.id.lec53);
        lectures[4][3] = findViewById(R.id.lec54);
        lectures[4][4] = findViewById(R.id.lec55);

        // すべてのLectureにOnClickListenerを登録
        for (Lecture[] lectures_ : lectures) {
            for (Lecture lecture : lectures_) {
                lecture.setOnClickListener(lectureOnClick);
            }
        }

        // 講義の詳細を前面に表示するView "LecInfoView"
        lecInfoView = findViewById(R.id.lecInfoView);
        lecInfoView.setContent(this);
        lecInfoView.setVisibility(View.GONE);
        // 背面のコンポーネントがタッチされても反応させないため、リスナーを登録しtrueを返す
        lecInfoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                return true;
            }
        });

//        final SwipeDismissBehavior behavior = new SwipeDismissBehavior();
//        behavior.setStartAlphaSwipeDistance(0.1f);
//        behavior.setEndAlphaSwipeDistance(0.6f);
//        behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
//        behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
//            @Override
//            public void onDismiss(View view) {
//
//            }
//
//            @Override
//            public void onDragStateChanged(int state) {
//                switch (state) {
//                    case SwipeDismissBehavior.STATE_DRAGGING:
//                        break;
//                    case SwipeDismissBehavior.STATE_IDLE:
//
//                }
//            }
//        });
//        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lecInfoView.getLayoutParams();
//        ((CoordinatorLayout.LayoutParams)params).setBehavior(behavior);
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);   // メニューの時間割を選択済みに

        // 時間割を設定
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        Set<String> lecCodes = preferences.getStringSet("lecCodes", null);  // 登録済みの講義の履修コードを取得

        if (lecCodes != null) {
            for (String lecCode : lecCodes) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> resultMap = gson.fromJson(preferences.getString(lecCode, ""), type);    // 講義情報を取得
                setLecture(resultMap);
            }
        }
    }

    private void setLecture(Map<String, Object> resultMap) {
        Map<String, Object> timeinfo = (Map<String, Object>) resultMap.get("timeinfo");
        for (Map.Entry<String, Object> entry : timeinfo.entrySet()) {
            Map<String, Object> map = (Map<String, Object>) entry.getValue();
            int week = Integer.parseInt(map.get("week").toString());
            int period = Integer.parseInt(map.get("period").toString());
            lectures[week-1][period-1].setLecInfo(resultMap);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editLectures:
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawers();
                Intent intent = new Intent(this, SearchLecture.class);
                startActivity(intent);
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return false;
    }

    private View.OnClickListener lectureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!((Lecture)v).isEmpty()) {
                lecInfoView.setLecture((Lecture)v);
                lecInfoView.setVisibility(View.VISIBLE);
                lecInfoView.setAlpha(0f);
                lecInfoView.animate().alpha(1f).setDuration(200).setListener(null);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lecInfoView.getVisibility() == View.VISIBLE) {
                hideLecInfoView();
                return true;
            }
        }
        return false;
    }

    private void hideLecInfoView() {
        ValueAnimator fadeOut = ObjectAnimator.ofFloat(lecInfoView, "alpha", 1f, 0f);
        fadeOut.setDuration(200);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lecInfoView.setVisibility(View.INVISIBLE);
            }
        });
        fadeOut.start();
    }
}