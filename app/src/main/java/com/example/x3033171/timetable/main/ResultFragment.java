package com.example.x3033171.timetable.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;
import com.example.x3033171.timetable.Todo.TodoData;
import com.example.x3033171.timetable.Todo.TodoRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class ResultFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView messege;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflate = inflater.inflate(R.layout.fragment_result, container, false);
        recyclerView = mInflate.findViewById(R.id.recyclerView);
        messege = mInflate.findViewById(R.id.messege);

        return mInflate;
    }

    public void setLecCode(String lecCode) {
        List<TodoData> dataList = new ArrayList<>();
        ArrayList<Map<String, String>> maps = Fun.readTodo(getContext(), lecCode);
        for (Map<String, String> map : maps) {
            dataList.add(new TodoData(map));
        }
        if (dataList.size() > 0) {
            messege.setVisibility(View.INVISIBLE);
        } else {
            messege.setVisibility(View.VISIBLE);
        }

        TodoRecyclerViewAdapter adapter = new TodoRecyclerViewAdapter(dataList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}