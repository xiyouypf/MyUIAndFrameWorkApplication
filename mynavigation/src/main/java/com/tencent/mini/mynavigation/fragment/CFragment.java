package com.tencent.mini.mynavigation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tencent.mini.mynavigation.R;

public class CFragment extends Fragment {
    private static final String TAG = "CFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        Log.d(TAG, "onCreateView: " + "name=" + name);
        return inflater.inflate(R.layout.c_fragment, container, false);
    }
}
