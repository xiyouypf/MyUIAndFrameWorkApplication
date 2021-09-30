package com.ypf.study.myrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;


public class DataAdapter extends MyRecyclerView.MyAdapter<DataAdapter.ViewHolder> {

    private List<String> list;

    public DataAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view_01, parent, false);
        return new ViewHolder(view);
    }

    @Override
    void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    int getItemCount() {
        return list.size();
    }

    class ViewHolder extends MyViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
