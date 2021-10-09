package com.ypf.study.myui.springback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.ypf.study.myui.R;

import java.util.ArrayList;
import java.util.List;

public class MySpringbackActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_springback);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new DataAdapter(this, getData()));
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("ypf" + (i + 1));
        }
        return list;
    }
}