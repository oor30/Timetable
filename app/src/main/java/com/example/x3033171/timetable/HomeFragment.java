package com.example.x3033171.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView lecName, teacher, room, text, grade, result;
    private String lecCode;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflater = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("HomeFragment#", "onCreateView");
        lecName = mInflater.findViewById(R.id.lecNamename);
        teacher = mInflater.findViewById(R.id.teacher);
        room = mInflater.findViewById(R.id.room);
        grade = mInflater.findViewById(R.id.grade);

        lecCode = getArguments().getString("lecCode");
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("pref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> resultMap = gson.fromJson(preferences.getString(lecCode, ""), type);    // 講義情報を取得
        int week = getArguments().getInt("week");
        int period = getArguments().getInt("period");

        lecName.setText(resultMap.get("授業科目名").toString());
        teacher.setText(resultMap.get("担当教員").toString());
        grade.setText(resultMap.get("対象学年").toString());

        Map<String, Map<String, String>> timeinfo = (Map<String, Map<String, String>>) resultMap.get("timeinfo");
        ArrayList<String> rooms = new ArrayList<>();
        for (Map<String, String> map : timeinfo.values()) {

        }
        room.setText(timeinfo.get("0").get("room"));
        // Inflate the layout for this fragment
        return mInflater;
    }

    public void setLecture(Lecture lecture) {
    }
}