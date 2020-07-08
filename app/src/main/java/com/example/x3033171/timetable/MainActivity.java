package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MainActivity main;
    DrawerLayout drawerLayout;
    CoordinatorLayout coordinatorLayout;
    FrameLayout frameLayout;
    Lecture[][] lectures;
    LecInfoView lecInfoView;
    NavigationView navigationView;
    private ViewPager pager;
    private PagerAdapter adapter;
    private int currentPage;
    private HomeFragment homeFragment;
    private ResultFragment resultFragment;
    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;
        drawerLayout = findViewById(R.id.drawerLayout);
//        layout = findViewById(R.id.cardView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        frameLayout = findViewById(R.id.frameLayout);

//        pager = findViewById(R.id.viewPager);
//        adapter = new PagerAdapter(getSupportFragmentManager());
//        pager.setAdapter(adapter);
//        currentPage = 0;
//
//        //instantiateItem()で今のFragmentを取得
//        homeFragment = (HomeFragment) adapter.instantiateItem(pager, 0);
//        resultFragment = (ResultFragment) adapter.instantiateItem(pager, 1);

        // ツールバー・検索ボックス
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

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

//        // 講義の詳細を前面に表示するView "LecInfoView"
//        lecInfoView = findViewById(R.id.lecInfoView);
//        lecInfoView.setMain(this);
//        behavior = BottomSheetBehavior.from(lecInfoView);
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior = BottomSheetBehavior.from(frameLayout);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                lectures[i][j].reset();
            }
        }

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
                return true;
        }
        return false;
    }

    private View.OnClickListener lectureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!((Lecture)v).isEmpty()) {
                Lecture lec = (Lecture)v;
                int week;
                int period = 0;
                outside: for (week=0; week<5; week++) {
                    for (period=0; period<5; period++) {
                        if (lec == lectures[week][period]) {
                            break outside;
                        }
                    }
                }
                // 講義の詳細を前面に表示するView "LecInfoView"
                frameLayout.removeAllViews();
                LecInfoView lecInfoView = new LecInfoView(main);
                lecInfoView.setMain(main);
                lecInfoView.setLecture(lec.getLecCode(), week, period);
                Log.d("Main#onClick", lec.getLecCode());
                frameLayout.addView(lecInfoView);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}