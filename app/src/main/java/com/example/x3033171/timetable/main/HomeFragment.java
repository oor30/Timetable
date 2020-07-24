package com.example.x3033171.timetable.main;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {

    private ConstraintLayout parent;
    private TextView teacher, room, grade, memo;
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

        parent = mInflater.findViewById(R.id.parent);
        memo = mInflater.findViewById(R.id.memo);
        setOutCursorListener(getContext(), memo, parent);
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

    private static void setOutCursorListener(final Context context, final TextView textView, final View parent) {
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(parent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    parent.requestFocus();
                    textView.clearFocus();
                }
                return false;
            }
        });
    }
}