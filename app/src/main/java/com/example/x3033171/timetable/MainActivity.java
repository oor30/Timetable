package com.example.x3033171.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Database database;
    Lecture[][] lectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        database = new Database(lectures);
        Intent intent = new Intent(getApplication(), SearchLecture.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume", "onResume()");
        try {
            FileInputStream fis = openFileInput("lecCode.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, String> map = new HashMap<>();
                map.put("履修コード", line);
                Log.d("lecCode", line);
                database.searchLecture(map, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}