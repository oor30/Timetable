package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SearchLecture extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // フィールド変数
    // レイアウト・サイドメニュー
    LinearLayout resultsLayout;
    DrawerLayout drawerLayout;
    ConstraintLayout constraintLayout;
    NavigationView navigationView;

    // View
    androidx.appcompat.widget.SearchView searchView;
    Button btFinish;
    Spinner spnFaculty, spnDepartment, spnCourse, spnGrade, spnWeek, spnPeriod;
    CheckBox selectedCB;
    ProgressBar progressBar;
    Space space1, space2;

    // 変数
    int grade, week, period;
    String name, faculty, department, course;
    ArrayList<String> bunrui;
    ArrayList<Result> resultArray;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lecture);

        // フィールド変数
        grade = week = period = 0;
        name = faculty = department = course = "";
        bunrui = new ArrayList<>(Arrays.asList("航空宇宙生産技術", "全学教職", "全学科対象科目", "複合領域", "工学部", faculty, department, course));
        resultArray = new ArrayList<>();

        // レイアウト・サイドメニュー
        drawerLayout = findViewById(R.id.drawerLayout2);
        constraintLayout = findViewById(R.id.slConstLay);
        resultsLayout = findViewById(R.id.linearLayout);
        navigationView = findViewById(R.id.navigationView2);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.editLectures);

        // ツールバー・検索ボックス
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
//        searchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        searchView = findViewById(R.id.search_bar);
//        searchView.setQueryHint("講義を検索");       // ヒントを表示
        searchView.setIconified(false);             // 検索ボックスを開いた状態にする
        searchView.clearFocus();                    // フォーカスを外す（これがないと一瞬キーボードが開く時がある）
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                name = query;
                showResults();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                name = newText;
                showResults();
                return false;
            }
        });

        // 講義編集完了ボタン
        btFinish = findViewById(R.id.btFinish);
        btFinish.setOnClickListener(onClickListener);

        // 所属・学年・曜日・時限スピナー
        spnFaculty = findViewById(R.id.spnFaculty);
        spnDepartment = findViewById(R.id.spnDepartment);
        spnCourse = findViewById(R.id.spnCourse);
        spnGrade = findViewById(R.id.spnGrade);
        spnWeek = findViewById(R.id.spnWeek);
        spnPeriod = findViewById(R.id.spnPeriod);
        spnFaculty.setOnItemSelectedListener(onItemSelectedListener);
        spnDepartment.setOnItemSelectedListener(onItemSelectedListener);
        spnCourse.setOnItemSelectedListener(onItemSelectedListener);
        spnGrade.setOnItemSelectedListener(onItemSelectedListener);
        spnWeek.setOnItemSelectedListener(onItemSelectedListener);
        spnPeriod.setOnItemSelectedListener(onItemSelectedListener);

        // チェックボックス
        selectedCB = findViewById(R.id.selectedCB);
        selectedCB.setOnClickListener(checkBoxListener);

        // プレグレスバー
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        space1 = findViewById(R.id.space1);
        space2 = findViewById(R.id.space2);

        // データベース
        database = new Database(this);
        database.searchLecture(null);
    }

    public static int convertDp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(dp * metrics.density);
    }

    // データベースで検索後、呼び出されるメソッド
    public void makeResults(Task<QuerySnapshot> task) {
        // 選択済みの教科を共有プリファレンスから取得
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        Set<String> lecCodes = preferences.getStringSet("lecCodes", null);

        // ドキュメントからResultインスタンスを作成・resultArray(ArrayList)に保存
        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
            Result result = new Result(this, document);
            if (lecCodes!=null && lecCodes.contains(result.getLecCode())) {
                result.setChecked(true);
            }
            Log.d("test", "結果を追加");
            resultArray.add(result);
        }

        // 結果を表示
        showResults();
        progressBar.setVisibility(View.INVISIBLE);  // 表示が終わったらプログレスバーを消す
    }

    // 結果を表示するメソッド
    public void showResults() {
        resultsLayout.removeAllViews();
        resultsLayout.addView(space1);
        resultsLayout.addView(space2);

        // 曜日・時限でソート
        Collections.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getPeriod() - o2.getPeriod();
            }
        });
        Collections.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getWeek() - o2.getWeek();
            }
        });

        // 結果をresultsLayoutに追加
        int weekTmp = 0;
        outside: for (Result result : resultArray) {
            // 条件に合わないものはcontinueして排除していく
            if (selectedCB.isChecked()) {   // 選択されているか
                if (!result.getChecked()) {
                    continue;
                }
            }
            if (!name.isEmpty()) {  // 検索ワードを含んでいるか
                String[] names = name.split(" ");   // スペースで区切る（or検索）
                for (String n : names) {
                    if (!result.getName().contains(n)) {
                        continue outside;
                    }
                }
            }
            if (spnFaculty.getSelectedItemPosition() > 0) {
                if (!bunrui.contains(result.getLecClass())) {
                    continue;
                }
            }
            if (grade > 0) {    // 学年
                if (result.getGrade() != grade) {
                    continue;
                }
            }
            if (week > 0) {     // 曜日
                if (result.getWeek() != week) {
                    continue;
                }
            }
            if (period > 0) {   // 時限
                if (result.getPeriod() != period) {
                    continue;
                }
            }
            if (weekTmp != result.getWeek()) {
                weekTmp = result.getWeek();
                TextView weekText = new TextView(this);
                weekText.setBackgroundColor(Color.parseColor("#EEEEEE"));
                weekText.setTextSize(18);
                weekText.setPadding(convertDp2Px(8, this), convertDp2Px(4, this), convertDp2Px(8, this), convertDp2Px(4, this));
                weekText.setText(String.valueOf(weekTmp).replace("1", "月曜日").replace("2", "火曜日")
                .replace("3", "水曜日").replace("4", "木曜日").replace("5", "金曜日"));
                resultsLayout.addView(weekText,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
            }
            resultsLayout.addView(result,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 講義編集完了ボタン
            if (v.getId() == R.id.btFinish) {   // 講義編集完了ボタン
                // 共有プリファレンスに選択した講義の①情報（Map）と②履修コード（String）を保存
                SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
                preferences.edit().clear().apply();     // 共有プリファレンス"pref"のデータを削除
                Gson gson = new Gson();
                HashSet<String> recCodes = new HashSet<>();     // 履修コードのHashSet

                // ①講義情報
                for (Result result : resultArray) {     // すべての対象の講義に対し
                    if (result.getChecked()) {          // 選択された講義は
                        Map<String, Object> resultMap = result.getResultMap();  // 講義情報（Map）を取り出し
                        preferences.edit().putString(result.getLecCode(), gson.toJson(resultMap)).apply();  // 履修コードをKeyとしてGsonを用いて保存

                        recCodes.add(result.getLecCode());  // その履修コードをHashSetに保存
                    }
                }

                // ②履修コード
                preferences.edit().putStringSet("lecCodes", recCodes).apply();

                // MainActivityに戻る
                finish();
    //                Intent intent = new Intent(getApplication(), MainActivity.class);
    //                startActivity(intent);
            }
        }
    };

    private View.OnClickListener checkBoxListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.selectedCB) {
                showResults();
            }
        }
    };

    // スピナーのイベント
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        // スピナーが変更されたとき
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent == spnFaculty) {     // 学部（不可視）
                faculty = spnFaculty.getSelectedItem().toString();
                if (position > 0) {     // デフォルトの選択肢ではないとき
                    spnDepartment.performClick();   // 学科スピナーを選択
                }
                else if(position == 0){
                    faculty = department = course = "";
                }
                bunrui.set(5, faculty);
            }
            else if (parent == spnDepartment) {     // 学科（不可視）
                department = spnDepartment.getSelectedItem().toString() + "工学科";
                if (position > 0) {
                    spnCourse.performClick();       // コーススピナーを選択
                }
                else if (position == 0) {
                    department = course = "";
                }
                bunrui.set(6, department);
            }
            else if (parent == spnCourse) {     // コース（不可視）
                course = spnCourse.getSelectedItem().toString() + "コース";
                if (position == 0) {
                    course = "";
                }
                bunrui.set(7, course);
            }
            else if (parent == spnGrade) {      // 学年
                grade = position;
            }
            else if (parent == spnWeek) {       // 曜日
                week = position;
            }
            else if (parent == spnPeriod) {     // 時限
                period = position;
            }
            showResults();

        }

        // スピナーに変更がなかったとき
        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    // サイドメニューNavigationViewのイベント
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {    // ホーム
            drawerLayout.closeDrawers();    // サイドメニューを閉じる
//            Intent intent = new Intent(getApplication(), MainActivity.class);   // MainActivityに戻る
//            startActivity(intent);
            finish();
        }
        else if (item.getItemId() == R.id.editLectures) {   // 講義編集（このアクティビティ）
            drawerLayout.closeDrawers();    // サイドメニューを閉じる
        }
        return true;
    }
}