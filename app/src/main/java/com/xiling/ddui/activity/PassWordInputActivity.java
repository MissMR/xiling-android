package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.adapter.EditAdapter;
import com.xiling.ddui.adapter.KeyAdapter;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.TextViewUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易密码输入页面
 */
public class PassWordInputActivity extends BaseActivity {
    //一共几位密码
    public final int PASSWORD_SIZE = 6;

    @BindView(R.id.recycler_edit)
    RecyclerView recyclerEdit;
    List<EditAdapter.EditBean> editList = new ArrayList<>();
    List<String> saveEditList = new ArrayList<>();
    EditAdapter editAdapter;

    @BindView(R.id.recycler_key)
    RecyclerView recyclerKey;
    KeyAdapter keyAdapter;
    List<String> keyList = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private String firstPassword, secondPassword;
    boolean isFirstInput = true;

    INewUserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word_input);
        ButterKnife.bind(this);
        setTitle("交易密码");
        setLeftBlack();
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();
    }

    private void initView() {
        LinearLayoutManager editLayoutMananger = new LinearLayoutManager(context);
        editLayoutMananger.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerEdit.setLayoutManager(editLayoutMananger);
        recyclerEdit.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(context, 4), 0));
        editList.clear();
        for (int i = 0; i < PASSWORD_SIZE; i++) {
            editList.add(new EditAdapter.EditBean());
        }

        editAdapter = new EditAdapter(editList);
        recyclerEdit.setAdapter(editAdapter);
        editAdapter.setNewData(editList);


        String[] keyArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "返回"};
        keyList = Arrays.asList(keyArray);
        GridLayoutManager keyLayoutMananger = new GridLayoutManager(context, 3);
        recyclerKey.setLayoutManager(keyLayoutMananger);
        keyAdapter = new KeyAdapter(keyList);
        recyclerKey.setAdapter(keyAdapter);
        /**
         * 键盘点击事件
         */

        keyAdapter.setOnKeyClickListener(new KeyAdapter.OnKeyClickListener() {
            @Override
            public void onKeyClick(String key) {

                if (key.equals("返回")) {
                    if (saveEditList.size() > 0) {
                        if (btnOk.isEnabled()) {
                            btnOk.setEnabled(false);
                        }
                        editList.get(saveEditList.size() - 1).setTitle("");
                        saveEditList.remove(saveEditList.size() - 1);
                        editAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                if (saveEditList.size() < PASSWORD_SIZE) {
                    editList.get(saveEditList.size()).setTitle(key);
                    saveEditList.add(key);
                    editAdapter.notifyDataSetChanged();

                    if (saveEditList.size() == PASSWORD_SIZE) {
                        //输入满了
                        btnOk.setEnabled(true);
                        String password = "";
                        for (String pw : saveEditList) {
                            password += pw;
                        }
                        inputComplete(password);
                    }

                }
            }
        });

    }

    private void inputComplete(String password) {
        if (isFirstInput) {
            firstPassword = password;
        } else {
            secondPassword = password;
        }
    }


    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        if (isFirstInput) {
            //第一次输入完成
            isFirstInput = false;
            for (int i = 0; i < editList.size(); i++) {
                editList.get(i).setTitle("");
            }
            saveEditList.clear();
            editAdapter.notifyDataSetChanged();
            tvTitle.setText("再次输入交易密码");
        } else {
            //第二次输入完成
            if (firstPassword.equals(secondPassword)) {
                //输入一致
                setAccountPassword(secondPassword);

            } else {
                //输入不一致
                ToastUtil.success("两次密码输入不一致，请重新输入");
                for (int i = 0; i < editList.size(); i++) {
                    editList.get(i).setTitle("");
                }
                saveEditList.clear();
                editAdapter.setNewData(editList);
                tvTitle.setText("输入6位交易密码");
                isFirstInput = true;
                firstPassword = "";
                secondPassword = "";
            }
        }
    }

    private void setAccountPassword(String password) {
        APIManager.startRequest(mUserService.setAccountPassword(password), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                ToastUtil.success(message);
                EventBus.getDefault().post(new EventMessage(Event.UPDE_PASSWORD));
                finish();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.success(e.getMessage());
            }
        });
    }


}
