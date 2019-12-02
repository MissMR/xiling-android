package com.dodomall.ddmall.module.product;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZujiItemFragment extends BaseFragment {


    public ZujiItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zuji_item, container, false);
    }

}
