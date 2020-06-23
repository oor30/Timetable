package com.example.x3033171.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SearchLecture extends AppCompatActivity {

    LinearLayout resultsLayout;
    TextView textView, belongs;
    Button btSearch, btFinish;
    Spinner spnFaculty, spnDepartment, spnCourse, spnGrade, spnWeek, spnPeriod;
    ProgressBar progressBar;

    int grade, week, period;
    String name, faculty, department, course;
    ArrayList<Result> resultArray;
    InputMethodManager inputMethodManager;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lecture);

        grade = week = period = 0;
        name = "";
        resultArray = new ArrayList<>();

        resultsLayout = (LinearLayout) findViewById(R.id.linearLayout);
        textView = findViewById(R.id.textView);
        textView.setOnKeyListener(onKeyListener);
        belongs = findViewById(R.id.belongs);
        belongs.setOnClickListener(onClickListener);
        btSearch = findViewById(R.id.btSearch);
        btSearch.setOnClickListener(onClickListener);
        btFinish = findViewById(R.id.btFinish);
        btFinish.setOnClickListener(onClickListener);


        spnFaculty = findViewById(R.id.spnFaculty);
        spnDepartment = findViewById(R.id.spnDepartment);
        spnCourse = findViewById(R.id.spnCourse);
        spnGrade = findViewById(R.id.spnGrade);
        spnWeek = findViewById(R.id.spnWeek);
        spnPeriod = findViewById(R.id.spnPeriod);
        spnDepartment.setVisibility(View.INVISIBLE);
        spnCourse.setVisibility(View.INVISIBLE);
        spnFaculty.setOnItemSelectedListener(onItemSelectedListener);
        spnDepartment.setOnItemSelectedListener(onItemSelectedListener);
        spnCourse.setOnItemSelectedListener(onItemSelectedListener);
        spnGrade.setOnItemSelectedListener(onItemSelectedListener);
        spnWeek.setOnItemSelectedListener(onItemSelectedListener);
        spnPeriod.setOnItemSelectedListener(onItemSelectedListener);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Map<String, String> map = new HashMap<>();
        database = new Database(this);
        database.searchLecture(map, 1);
    }

    public void makeResults(Task<QuerySnapshot> task) {
        ArrayList<String> lecCodes = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("lecCode.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                lecCodes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
            Result result = new Result(this, document);
            if (lecCodes.contains(result.getLecCode())) {
                result.setChecked(true);
            }
            resultArray.add(result);
        }
        showResults(resultArray, faculty, department, course, 0, 0, 0, "");
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showResults(ArrayList<Result> resultArray, String faculty, String department, String course,
                            int grade, int week, int period, String name) {
        resultsLayout.removeAllViews();
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
        for (Result result : resultArray) {
            if (!name.isEmpty()) {
                if (!result.getName().contains(name)) {
                    continue;
                }
            }
            if (spnFaculty.getSelectedItemPosition() > 0 && spnDepartment.getSelectedItemPosition() > 0
                    && spnCourse.getSelectedItemPosition() > 0) {
                if (!Arrays.asList(new String[]{faculty, department, course, "全学科対象科目"})
                        .contains(result.getLecClass())) {
                    continue;
                }
            }
            if (grade > 0) {
                if (result.getGrade() != grade) {
                    continue;
                }
            }
            if (week > 0) {
                if (result.getWeek() != week) {
                    continue;
                }
            }
            if (period > 0) {
                if (result.getPeriod() != period) {
                    continue;
                }
            }
            resultsLayout.addView(result,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btSearch) {
                name = textView.getText().toString();
                showResults(resultArray, faculty, department, course, grade, week, period, name);
                inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
            else if (v.getId() == R.id.btFinish) {
                StringBuilder lecCode = new StringBuilder();
                for (Result result : resultArray) {
                    if (result.getChecked()) {
                        lecCode.append(result.getLecCode()).append("\n");
                    }
                }
                try {
                    FileOutputStream fos = openFileOutput("lecCode.txt", Context.MODE_PRIVATE);
                    fos.write(lecCode.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
            else if (v.getId() == R.id.belongs) {
                spnFaculty.performClick();
            }
        }
    };

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                onClickListener.onClick(btSearch);
            }
            return true;
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if ((Spinner)parent == spnFaculty) {
                faculty = spnFaculty.getSelectedItem().toString();
                if (position > 0) {
                    spnDepartment.performClick();
                }
                belongs.setText(String.format("%s", faculty));
            }
            else if ((Spinner)parent == spnDepartment) {
                department = spnDepartment.getSelectedItem().toString();
                if (position > 0) {
                    spnCourse.performClick();
                    belongs.setText(String.format("%s/%s", faculty, department));
                }
            }
            else if ((Spinner)parent == spnCourse) {
                course = spnCourse.getSelectedItem().toString();
                onClickListener.onClick(btSearch);
                belongs.setText(String.format("%s/%s/%s", faculty, department, course));
            }
            else if ((Spinner)parent == spnGrade) {
                grade = position;
                onClickListener.onClick(btSearch);
            }
            else if ((Spinner)parent == spnWeek) {
                week = position;
                onClickListener.onClick(btSearch);
            }
            else if ((Spinner)parent == spnPeriod) {
                period = position;
                onClickListener.onClick(btSearch);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}