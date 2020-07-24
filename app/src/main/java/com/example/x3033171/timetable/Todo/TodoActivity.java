package com.example.x3033171.timetable.Todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        List<TodoData> dataList = new ArrayList<>();
        ArrayList<ArrayList<Map<String, String>>> todo = Fun.readAllTodo(this);
        for (ArrayList<Map<String, String>> maps : todo) {
            for (Map<String, String> map : maps) {
                dataList.add(new TodoData(map));
            }
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TodoRecyclerViewAdapter adapter = new TodoRecyclerViewAdapter(dataList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}