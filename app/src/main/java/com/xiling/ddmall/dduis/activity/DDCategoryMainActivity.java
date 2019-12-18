package com.xiling.ddmall.dduis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.fragment.DDCategoryFragment;
import com.xiling.ddmall.module.search.SearchActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;


public class DDCategoryMainActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_for_activity);

        initView();

        DDCategoryFragment categoryFragment = new DDCategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, categoryFragment).commit();
    }

    public void initView() {
        setTitle("分类");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHeaderLayout().setRightDrawable(R.mipmap.icon_category_title_bar_search);
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
