package com.xiling.ddmall.module.store;

import com.xiling.ddmall.module.auth.event.BaseMsg;
import com.xiling.ddmall.shared.bean.MemberStore;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/23.
 */
public class MsgStore extends BaseMsg{
    public static final int ACTION_SEND_STORE_OBJ = 1 << 0;
    private MemberStore mMemberStore;

    public MsgStore(int action) {
        super(action);
    }


    public void setMemberStore(MemberStore memberStore) {
        mMemberStore = memberStore;
    }

    public MemberStore getMemberStore() {
        return mMemberStore;
    }
}
