package com.example.x3033171.timetable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kodmap.library.kmrecyclerviewstickyheader.KmStickyListener;


public class ResultRecyclerViewAdapter extends ListAdapter<Model, RecyclerView.ViewHolder> implements KmStickyListener {

    public ResultRecyclerViewAdapter() {
        super(ModelDiffUtilCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ItemType.Header) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_post, viewGroup, false);
            return new PostViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == ItemType.Header) {
            ((HeaderViewHolder) viewHolder).bind(getItem(i));
        } else {
            ((PostViewHolder) viewHolder).bind(getItem(i));
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.week_header);
        }

        public void bind(Model model) {
            title.setText(model.week);
        }
    }
    class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView resName, resWeek, resPeriod;
        public CheckBox addLecCB;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            resWeek = itemView.findViewById(R.id.resWeek);
            resPeriod = itemView.findViewById(R.id.resPeriod);
            resName = itemView.findViewById(R.id.resName);
            addLecCB = itemView.findViewById(R.id.addLecCB);
        }

        public void bind(Model model) {
            resWeek.setText(model.grade);
            resPeriod.setText(model.periods.toString().replace("[", "")
                    .replace("]", "") + "限");
            resName.setText(model.name);
            addLecCB.setChecked(model.checked);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public Integer getHeaderPositionForItem(Integer itemPosition) {
        Integer headerPosition = 0;
        for (Integer i = itemPosition;i > 0 ;i--){
            if (isHeader(i)){
                headerPosition = i;
                return headerPosition;
            }
        }
        return headerPosition;
    }

    @Override
    public Integer getHeaderLayout(Integer headerPosition) {
        return R.layout.result_header;
    }

    @Override
    public void bindHeaderData(View header, Integer headerPosition) {
        TextView textView = header.findViewById(R.id.week_header);
        try {
            textView.setText(getItem(headerPosition).title);
        } catch (Exception e) {
            textView.setText("null");
        }
    }

    @Override
    public Boolean isHeader(Integer itemPosition) {
        try {
            return getItem(itemPosition).type.equals(ItemType.Header);
        } catch (Exception e) {
//            Log.w(TAG, "isHeader: error");
            return false;
        }
    }

    public static final DiffUtil.ItemCallback<Model> ModelDiffUtilCallback =
            new DiffUtil.ItemCallback<Model>() {
                @Override
                public boolean areItemsTheSame(@NonNull Model model, @NonNull Model t1) {
                    return model.title.equals(t1.title);
                }

                @Override
                public boolean areContentsTheSame(@NonNull Model model, @NonNull Model t1) {
                    return model.equals(t1);
                }
            };
}
