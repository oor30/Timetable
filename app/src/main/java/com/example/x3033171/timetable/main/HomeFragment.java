package com.example.x3033171.timetable.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.x3033171.timetable.R;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {

    private TextView teacher, room, grade;
    Button btSetGrade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflater = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("HomeFragment#", "onCreateView");
        teacher = mInflater.findViewById(R.id.teacher);
        room = mInflater.findViewById(R.id.room);
        grade = mInflater.findViewById(R.id.grade);
        btSetGrade = mInflater.findViewById(R.id.btSetGrade);
        btSetGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeDialogFragment gdf = new GradeDialogFragment();
                gdf.show(getChildFragmentManager(), "dialog");
            }
        });
        return mInflater;
    }

    public void setLecture(Map<String, Object> resultMap) {
        if (resultMap != null && !resultMap.isEmpty()) {
            teacher.setText(String.valueOf(resultMap.get("担当教員")));
            grade.setText(String.valueOf(resultMap.get("対象学年")));

            Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
            ArrayList<String> rooms = new ArrayList<>();
            if (timeinfo != null) {
                for (Map<String, Object> map : timeinfo.values()) {
                    String roomTmp = String.valueOf(map.get("room"));
                    if (!roomTmp.equals("null") && !rooms.contains(roomTmp)) {
                        rooms.add(roomTmp);
                    }
                }
            }
            room.setText(rooms.toString().replace("[", "").replace("]", ""));
        }
    }
}