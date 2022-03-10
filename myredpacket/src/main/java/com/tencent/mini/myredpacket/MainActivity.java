package com.tencent.mini.myredpacket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button grabRedEnvelope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grabRedEnvelope = findViewById(R.id.grab_red_envelope);
        grabRedEnvelope.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.grab_red_envelope) {
            Intent intent = new Intent();
            String action = Settings.ACTION_ACCESSIBILITY_SETTINGS;
            intent.setAction(action);
            startActivity(intent);
        }
    }
}