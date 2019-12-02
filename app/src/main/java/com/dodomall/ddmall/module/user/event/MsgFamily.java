package com.dodomall.ddmall.module.user.event;

import com.dodomall.ddmall.shared.basic.BaseEventMsg;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class MsgFamily extends BaseEventMsg{

    public final static int ACTION_FAMILY_MENBER_TYPE_CLICK = 1<<0;

    private int fmailyType;
    private int memberType;

    public MsgFamily(int action) {
        super(action);
    }

    public int getFmailyType() {
        return fmailyType;
    }

    public void setFmailyType(int fmailyType) {
        this.fmailyType = fmailyType;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }
}
