package com.eric.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PermissionsFragment extends Fragment {

    public PermissionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment,container,false);
        view.findViewById(R.id.txt_show);
        return view;
    }
}
