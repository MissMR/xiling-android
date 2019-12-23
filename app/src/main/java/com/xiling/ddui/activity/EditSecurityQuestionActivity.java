package com.xiling.ddui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xiling.R;
import com.xiling.ddui.bean.DQuestionBean;
import com.xiling.ddui.bean.QuestionBean;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.OptionsPickerDialogManage;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 编辑安全问题页面
 */
public class EditSecurityQuestionActivity extends BaseActivity {

    public interface DataLoadListener {
        void onLoadEnd();
    }

    @BindView(R.id.tv_btn_save)
    TextView mTvBtnSave;
    @BindView(R.id.tv_select_question)
    TextView tvQuestion;

    @BindView(R.id.et_answer)
    EditText etAnswer;

    ArrayList<QuestionBean> options = new ArrayList<>();

    IUserService mIUserService = null;
    UserAuthBean authBean = null;

    int selectIndex = -1;
    int qId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sequrity_question);
        ButterKnife.bind(this);
        showHeader("安全问题");

        mIUserService = ServiceManager.getInstance().createService(IUserService.class);

        loadUserConfig();

        mTvBtnSave.setEnabled(false);
        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSaveButtonEnable(selectIndex > -1 && s.toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.tv_select_question, R.id.tv_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_question:
                showQuestionList();
                break;
            case R.id.tv_btn_save:
                uplodSafeQuestion();
                break;
        }
    }

    public void showQuestionList() {
        DLog.i("options:" + options);
        if (options.size() > 0) {
            showOptions();
        } else {
            loadSafeQuestionList(new DataLoadListener() {
                @Override
                public void onLoadEnd() {
                    showOptions();
                }
            });
        }
    }

    public void showOptions() {
        OptionsPickerView<QuestionBean> optionView = OptionsPickerDialogManage.getOptionsDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                DLog.i("options1:" + options1 + ",options2:" + options2 + ",options3：" + options3);
                selectIndex = options1;
                setSelectData();
                setSaveButtonEnable(selectIndex > -1 && etAnswer.getText().length() > 0);

            }
        });
        optionView.setPicker(options);
        optionView.show();
    }

    private void setSaveButtonEnable(boolean isEnable) {
        mTvBtnSave.setEnabled(isEnable);
    }

    public void setSelectData() {
        if (selectIndex < options.size()) {
            QuestionBean q = options.get(selectIndex);
            qId = q.getqId();
            tvQuestion.setText("" + q.getqTitle());
        }
    }

    /**
     * 加载用户现在的状态
     */
    public void loadUserConfig() {
        APIManager.startRequest(mIUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                authBean = result;
                loadSafeQuestionList(null);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    /**
     * 加载问题列表
     */
    public void loadSafeQuestionList(final DataLoadListener listener) {
        APIManager.startRequest(mIUserService.getSafeQuestion(), new RequestListener<DQuestionBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DQuestionBean result) {
                super.onSuccess(result);
                options = result.getList();
                options.remove(0);
                if (listener != null) {
                    listener.onLoadEnd();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 提交安全问题
     */
    public void uplodSafeQuestion() {

        if (qId < 0) {
            ToastUtil.error("请先选择安全问题");
            return;
        }

        String value = etAnswer.getText() + "";
        if (value.length() <= 0) {
            ToastUtil.error("必须填写安全问题答案");
            return;
        }

        APIManager.startRequest(mIUserService.setSafeQuestion(qId, value), new RequestListener<Object>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result);
                ToastUtil.success("" + msg);
                finish();

                UIEvent event = new UIEvent();
                event.setType(UIEvent.Type.CloseQuestionActivity);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
