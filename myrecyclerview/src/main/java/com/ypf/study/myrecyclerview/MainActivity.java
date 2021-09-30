package com.ypf.study.myrecyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new DataAdapter(getData()));
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("ypf" + i);
        }
        return list;
    }
}