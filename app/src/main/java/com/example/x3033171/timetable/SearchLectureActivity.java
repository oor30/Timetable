package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SearchLectureActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // フィールド変数
    // レイアウト・サイドメニュー
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;

    // View
    androidx.appcompat.widget.SearchView searchView;
    Button btFinish;
    Spinner spnFaculty, spnDepartment, spnCourse, spnGrade, spnWeek, spnPeriod;
    CheckBox selectedCB;
    ProgressBar progressBar;

    // 変数
    int grade, week, period;
    String name, faculty, department, course;
    ArrayList<String> bunrui;
    ArrayList<Result> resultArray;
    Database database;
    Set<String> selectedLecCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lecture);

        // フィールド変数
        grade = week = period = 0;
        name = faculty = department = course = "";
        bunrui = new ArrayList<>(Arrays.asList("航空宇宙生産技術", "全学教職", "全学科対象科目", "複合領域", "工学部", faculty, department, course));
        resultArray = new ArrayList<>();
        selectedLecCode = new HashSet<>();

        // レイアウト・サイドメニュー
        drawerLayout = findViewById(R.id.drawerLayout2);
        NavigationView navigationView = findViewById(R.id.navigationView2);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.editLectures);

        recyclerView = findViewById(R.id.recyclerView);


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
        searchView = findViewById(R.id.search_bar);
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

        // データベース
        database = new Database(this);
        database.searchLecture(spnFaculty.getSelectedItem().toString());
    }

    public static int convertDp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(dp * metrics.density);
    }

    private List<Result> createDataset(QueryDocumentSnapshot document) {
        List<Result> dataset = new ArrayList<>();
        for (int i=0; i<50; i++) {
            Result result = new Result(document);
            dataset.add(result);
        }
        return dataset;
    }

    // データベースで検索後、呼び出されるメソッド
    public void makeResults(Task<QuerySnapshot> task) {
        // 選択済みの教科を共有プリファレンスから取得
        Set<String> lecCodes = Fun.readAllLecCodes(this);

        // ドキュメントからResultインスタンスを作成・resultArray(ArrayList)に保存
        Log.d("SearchLecture", "結果を追加中");
        resultArray.clear();
        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
            Result result = new Result(document);
            if (lecCodes != null && lecCodes.contains(result.getLecCode())) {
                result.setChecked(true);
            }
            resultArray.add(result);
        }

        // 結果を表示
        showResults();
        progressBar.setVisibility(View.INVISIBLE);  // 表示が終わったらプログレスバーを消す
    }

    // 結果を表示するメソッド
    public void showResults() {
        // 曜日・時限でソート
        Collections.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getPeriods().iterator().next() - o2.getPeriods().iterator().next();
            }
        });
        Collections.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getGrade() - o2.getGrade();
            }
        });
        Collections.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getWeeks().iterator().next() - o2.getWeeks().iterator().next();
            }
        });

        // 結果をresultsLayoutに追加
        List<Model> resultArray_ = new ArrayList<>();
        Integer weekTmp = 0;
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
//            if (spnFaculty.getSelectedItemPosition() > 0) {
//                if (!bunrui.contains(result.getLecClass())) {
//                    continue;
//                }
//            }
            if (grade > 0) {    // 学年
                if (result.getGrade() != grade) {
                    continue;
                }
            }
            if (week > 0) {     // 曜日
                if (!result.getWeeks().contains(week)) {
                    continue;
                }
            }
            if (period > 0) {   // 時限
                if (!result.getPeriods().contains(period)) {
                    continue;
                }
            }

            // 曜日ヘッダーを追加
            if (!result.getWeeks().iterator().next().equals(weekTmp)) {
                weekTmp = result.getWeeks().iterator().next();
                String week = String.valueOf(weekTmp).replace("1", "月曜日").replace("2", "火曜日")
                        .replace("3", "水曜日").replace("4", "木曜日")
                        .replace("5", "金曜日").replace("6", "未定");
                resultArray_.add(new Model(week));
            }
            resultArray_.add(new Model(result));
        }
        ResultRecyclerViewAdapter adapter = new ResultRecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        KmHeaderItemDecoration kmHeaderItemDecoration = new KmHeaderItemDecoration(adapter);
        recyclerView.setAdapter(adapter);
        for (Model model : resultArray_) {
            if (model.type.equals(ItemType.Header)) {
                Log.d("SearchLecture", model.week);
            }
            else {
                Log.d("SearchLecture", model.name);
            }
        }
        if (resultArray_.size() > 0) {
            adapter.submitList(resultArray_);
        }
        Log.d("SearchLecture", "結果を追加");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 講義編集完了ボタン
            if (v.getId() == R.id.btFinish) {   // 講義編集完了ボタン
                Log.d("SearchLecture", "onClick: ");
                ArrayList<Map<String, Object>> resultMaps = new ArrayList<>();
                for (Result result : resultArray) {
                    if (result.getChecked()) {
                        resultMaps.add(result.getResultMap());
                    }
                }
                Fun.writeLecInfo(getApplicationContext(), resultMaps);
                finish();
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
//                if (position > 0) {     // デフォルトの選択肢ではないとき
//                    spnDepartment.performClick();   // 学科スピナーを選択
//                }
//                else if(position == 0){
//                    faculty = department = course = "";
//                }
                bunrui.set(5, faculty);
                progressBar.setVisibility(View.VISIBLE);
                database.searchLecture(faculty);
                return;
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
            else if (parent == spnGrade) grade = position;      // 学年
            else if (parent == spnWeek) week = position;        // 曜日
            else if (parent == spnPeriod) period = position;    // 時限
            showResults();
        }

        // スピナーに変更がなかったとき
        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    // サイドメニューNavigationViewのイベント
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();    // サイドメニューを閉じる
        switch (item.getItemId()) {
            case R.id.home:     // ホーム
                finish();
                break;
            case R.id.myLec:
                Intent intent = new Intent();

                break;
            case R.id.editLectures:    // 講義編集（このアクティビティ）
                break;
            case R.id.readHtml:

                break;
        }
        return true;
    }
}