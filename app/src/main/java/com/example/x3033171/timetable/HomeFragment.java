package com.example.x3033171.timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

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

    private TextView teacher, room, grade;
    Button btSetGrade;

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