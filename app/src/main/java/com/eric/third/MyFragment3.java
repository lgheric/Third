package com.eric.third;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MyFragment3 extends Fragment {
    public MyFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        TextView txt_content = view.findViewById(R.id.txt_content);
        txt_content.setText("第三个Fragment");
        Log.e("HEHE", "3日狗");
        return view;
    }
}
