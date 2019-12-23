package com.xiling.module.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xiling.R;
import com.xiling.ddui.fragment.DDCartFragment;
import com.xiling.shared.constant.AppTypes;

import static com.xiling.ddui.fragment.DDCartFragment.TYPE_ACTIVITY;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.cart
 * @since 2017-08-07
 */
public class CartActivity extends FragmentActivity {

    DDCartFragment cartFragment = DDCartFragment.newInstance(TYPE_ACTIVITY);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        buildNewCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void buildOldCart() {
        CartFragment cartFragment = CartFragment.newInstance(AppTypes.CART.FROM_ACTIVITY);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, cartFragment);
        fragmentTransaction.commit();
    }

    public void buildNewCart() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, cartFragment);
        fragmentTransaction.commit();
    }
}
