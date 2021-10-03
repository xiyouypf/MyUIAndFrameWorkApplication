package com.ypf.study.myrecyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MyTowAdapter(10));
    }

    class MyTextViewHolder extends MyViewHolder {
        TextView textView;

        public MyTextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text1);
        }
    }

    class MyOneAdapter extends MyRecyclerView.MyAdapter<MyTextViewHolder> {
        int count;

        public MyOneAdapter(int count) {
            this.count = count;
        }

        @Override
        MyTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table1,
                    parent, false);
            return new MyTextViewHolder(view);
        }

        @Override
        void onBindViewHolder(@NonNull MyTextViewHolder holder, int position) {
            holder.textView.setText("ypf");
        }

        @Override
        int getItemCount() {
            return count;
        }

        @Override
        public int getHeight() {
            return 50;
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    class MyImageViewHolder extends MyViewHolder {
        TextView textView;
        ImageView imageView;

        public MyImageViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text2);
            imageView = itemView.findViewById(R.id.img);
        }
    }

    class MyTowAdapter extends MyRecyclerView.MyAdapter<MyViewHolder> {
        int count;

        public MyTowAdapter(int count) {
            this.count = count;
        }

        @Override
        MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table1, parent, false);
                return new MyTextViewHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table2, parent, false);
                return new MyImageViewHolder(view);
            }
        }

        @Override
        void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (getItemViewType(position) == 0) {
                MyTextViewHolder myTextViewHolder = (MyTextViewHolder) holder;
                myTextViewHolder.textView.setText("布局1");
            } else {
                MyImageViewHolder myImageViewHolder = (MyImageViewHolder) holder;
                myImageViewHolder.textView.setText("布局2");
            }
        }

        @Override
        int getItemCount() {
            return count;
        }

        @Override
        public int getHeight() {
            return 200;
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}