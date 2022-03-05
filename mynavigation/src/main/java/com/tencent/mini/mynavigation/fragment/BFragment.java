package com.tencent.mini.mynavigation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tencent.mini.mynavigation.R;

public class BFragment extends Fragment {

    private static final String TAG = "BFragment";
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        String name_ = getArguments().getString("name_");
        Log.d(TAG, "onCreateView: " + "name=" + name);
        Log.d(TAG, "onCreateView: " + "name_=" + name_);
        return inflater.inflate(R.layout.b_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "from BFragment");
                Navigation.findNavController(getView()).navigate(R.id.action_bfragment_to_cfragment2, bundle);
            }
        });
    }
}
