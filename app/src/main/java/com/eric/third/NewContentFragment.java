package com.eric.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NewContentFragment extends Fragment {

    NewContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        TextView txt_content =  view.findViewById(R.id.txt_content);
        //getArgument获取传递过来的Bundle对象
        assert getArguments() != null;
        txt_content.setText(getArguments().getString("content"));
        return view;
    }

}
