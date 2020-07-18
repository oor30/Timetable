package com.example.x3033171.timetable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultViewHolder> {

    private List<Result> results;

    public ResultRecyclerViewAdapter(List<Result> list) {
        results = list;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_layout, parent, false);
        return new ResultViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultViewHolder holder, final int position) {
        holder.resWeek.setText(results.get(position).getWeeks().toString());
        holder.resPeriod.setText(results.get(position).getPeriods().toString());
        holder.resName.setText(results.get(position).getName());
        if (results.get(position).getChecked()) {
            holder.addLecCB.setChecked(true);
        }
        holder.addLecCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.get(position).setChecked(holder.addLecCB.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
