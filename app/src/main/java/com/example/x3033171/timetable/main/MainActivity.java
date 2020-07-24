package com.example.x3033171.timetable.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.x3033171.timetable.LecturePref;
import com.example.x3033171.timetable.Todo.TodoActivity;
import com.example.x3033171.timetable.TodoDatabase;
import com.example.x3033171.timetable.TodoPref;
import com.example.x3033171.timetable.searchLecture.Lecture;
import com.example.x3033171.timetable.myLectures.MyLecturesActivity;
import com.example.x3033171.timetable.R;
import com.example.x3033171.timetable.searchLecture.SearchLectureActivity;
import com.example.x3033171.timetable.webView.WebViewActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LecturePref, TodoPref, TodoDatabase {

    private DrawerLayout drawerLayout;
    private Lecture[][] lectures;
    private LecInfoView lecInfoView;
    private ViewPager pager;
    private PagerAdapter adapter;
    private HomeFragment homeFragment;
    ResultFragment resultFragment;
    private BottomSheetBehavior<LecInfoView> behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);

        pager = findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);

        //instantiateItem()で今のFragmentを取得
        homeFragment = (HomeFragment) adapter.instantiateItem(pager, 0);
        resultFragment = (ResultFragment) adapter.instantiateItem(pager, 1);

        // ツールバー
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

        // 講義の詳細を前面に表示するView "LecInfoView"
        lecInfoView = findViewById(R.id.lecInfoView);
        lecInfoView.getTabLayout().setupWithViewPager(pager);
        lecInfoView.setMain(this);
        behavior = BottomSheetBehavior.from(lecInfoView);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        SharedPreferences preferences = getSharedPreferences("pref-version", MODE_PRIVATE);
        int oldVer = preferences.getInt("version", 0);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            int newVer = pi.versionCode;
            preferences.edit().putInt("version", newVer).apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (oldVer < 1) {
            preferences = getSharedPreferences("pref-todo", MODE_PRIVATE);
            preferences.edit().clear().apply();
            preferences = getSharedPreferences("pref-lecInfo", MODE_PRIVATE);
            preferences.edit().clear().apply();
            preferences = getSharedPreferences("pref-lecCodes", MODE_PRIVATE);
            preferences.edit().clear().apply();
        }

        Set<String> lecCodes = readLecCodes(this);
        for (String lecCode : lecCodes) {
            subsSnapshotListener(this, lecCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);   // メニューの時間割を選択済みに

        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                lectures[i][j].reset();
            }
        }

        // 時間割を設定
        Set<String> lecCodes = readLecCodes(this);
        ArrayList<Map<String, Object>> resultMaps = readLecInfo(this, lecCodes);
        for (Map<String, Object> resultMap : resultMaps) {
            setLecture(resultMap);
        }
    }

    private void setLecture(Map<String, Object> resultMap) {
        Map<String, Object> timeinfo = (Map<String, Object>) resultMap.get("timeinfo");
        if (timeinfo != null) {
            for (Map.Entry<String, Object> entry : timeinfo.entrySet()) {
                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                String weekTmp = String.valueOf(map.get("week"));
                int week;
                if (!weekTmp.equals("null")) week = (int) Double.parseDouble(weekTmp);
                else break;
                String periodTmp = String.valueOf(map.get("period"));
                int period;
                if (!periodTmp.equals("null")) period = (int) Double.parseDouble(periodTmp);
                else break;
                if (week != 0 && period != 0) {
                    lectures[week-1][period-1].setLecInfo(resultMap);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {        switch (item.getItemId()) {
        case R.id.editLectures:
            drawerLayout.closeDrawers();
            Intent intent = new Intent(this, SearchLectureActivity.class);
            startActivity(intent);
            return true;
        case R.id.readHtml:
            drawerLayout.closeDrawers();
            Intent intent1 = new Intent(this, WebViewActivity.class);
            startActivity(intent1);
            return true;
        case R.id.myLec:
            drawerLayout.closeDrawers();
            Intent intent2 = new Intent(this, MyLecturesActivity.class);
            startActivity(intent2);
            return true;
        case R.id.todo:
            drawerLayout.closeDrawers();
            Intent intent3 = new Intent(this, TodoActivity.class);
            startActivity(intent3);
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
                int period;
                outside: for (week=0; week<5; week++) {
                    for (period=0; period<5; period++) {
                        if (lec == lectures[week][period]) {
                            break outside;
                        }
                    }
                }
                setLecInfoView(lec, week);
            }
            else {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    };

    public void setLecInfoView(Lecture lec, int week) {
        // 講義の詳細を前面に表示するView "LecInfoView"
        lecInfoView.setLecName(lec.getLecName());
        lecInfoView.setLecCode(lec.getLecCode());
        lecInfoView.setWeek(week);
        homeFragment = (HomeFragment) adapter.instantiateItem(pager, 0);
        homeFragment.setLecture(lec.getResultMap());
        resultFragment = (ResultFragment) adapter.instantiateItem(pager, 1);
        resultFragment.setLecCode(lec.getLecCode());
        Log.d("Main#onClick", lec.getLecCode());
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

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

    @Override
    public void OnGetTodoMap(Map<String, String> todoMap) {
        boolean wrote = writeTodo(this, todoMap);
        if (wrote) {
            Log.d(TAG, "課題を追加：" + todoMap);
        } else {
            Log.d(TAG, "課題がすでに存在しています：" + todoMap);
        }
    }

    @Override
    public void OnCreateGlobalTodo(@NotNull Map<String, String> todoMap, String lecCode) {
        upTodo(todoMap, lecCode);
    }
}