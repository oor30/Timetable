package com.example.x3033171.timetable.Todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<TodoData> list;

    public TodoRecyclerViewAdapter(List<TodoData> list) {
        Collections.sort(list, new TodoDataComparator());
        this.list = list;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new TodoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDate());
        boolean allDay = list.get(position).isAllDay();
        if(!allDay) {
            holder.time.setText(list.get(position).getTime());
        } else {
            holder.time.setText("終日");
        }
        holder.name.setText(list.get(position).getName());
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class TodoDataComparator implements Comparator<TodoData> {
        @Override
        public int compare(TodoData o1, TodoData o2) {
            Calendar o1Cal = o1.getCalendar();
            Calendar o2Cal = o2.getCalendar();

            return o1Cal.compareTo(o2Cal);
        }
    }
}
