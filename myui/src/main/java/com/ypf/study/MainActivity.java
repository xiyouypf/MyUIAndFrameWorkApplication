package com.ypf.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ypf.study.myui.R;
import com.ypf.study.myui.myflowlayout.MyFlowLayoutActivity;
import com.ypf.study.myui.mypercentlayout.MyPercentRelativeLayoutActivity;
import com.ypf.study.myui.springback.MySpringbackActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button flowButton;
    private Button percentButton;
    private Button springbackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
    }

    private void initButtons() {
        flowButton = findViewById(R.id.flow_button);
        percentButton = findViewById(R.id.percent_button);
        springbackButton = findViewById(R.id.springback_button);
        flowButton.setOnClickListener(this);
        percentButton.setOnClickListener(this);
        springbackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.flow_button) {
            Intent intent = new Intent(this, MyFlowLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.percent_button) {
            Intent intent = new Intent(this, MyPercentRelativeLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.springback_button) {
            Intent intent = new Intent(this, MySpringbackActivity.class);
            startActivity(intent);
        }
    }
}