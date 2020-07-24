package com.example.x3033171.timetable.overLecDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x3033171.timetable.LecturePref;
import com.example.x3033171.timetable.R;
import com.example.x3033171.timetable.myLectures.MyLecRecyclerViewAdapter;
import com.example.x3033171.timetable.searchLecture.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class OverLecDialogFragment extends DialogFragment implements LecturePref {
    RecyclerView recyclerView;
    MyLecRecyclerViewAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_over_lec_dialog);
        dialog.setTitle("課題が重複しています");

        ArrayList<String> lecCodes = getArguments().getStringArrayList("lecCodes");

        recyclerView = dialog.findViewById(R.id.recyclerView);
        adapter = new MyLecRecyclerViewAdapter(readMyLec(lecCodes));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return dialog;
    }

    private List<Model> readMyLec(ArrayList<String> lecCodes) {
        List<Model> dataset = new ArrayList<>();
        ArrayList<Map<String, Object>> resultMaps = readLecInfo(getContext(), new HashSet<>(lecCodes));
        for (Map<String, Object> resultMap : resultMaps) {
            if (lecCodes.contains(String.valueOf(resultMap.get("履修コード")))) {
                dataset.add(new Model(resultMap, true));
            }
            else {
                dataset.add(new Model(resultMap, false));
            }
        }
        return dataset;
    }
}
