package com.dodomall.ddmall.module.instant;

import com.dodomall.ddmall.shared.basic.BaseEventMsg;
import com.dodomall.ddmall.shared.bean.InstantData;

import java.util.ArrayList;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/3.
 */
public class MsgInstant extends BaseEventMsg{

    public static final int ACTION_SEND_SECOND_KILL = 1<<0;
    public static final int ACTION_BUY = 1<<1;

    private InstantData.SecondKill mSecondKill;
    private ArrayList<InstantData.SecondKill> mSecondKills;

    public MsgInstant(int action) {
        super(action);
    }

    public InstantData.SecondKill getSecondKill() {
        return mSecondKill;
    }

    public void setSecondKill(InstantData.SecondKill secondKill) {
        mSecondKill = secondKill;
    }

    public void setSecondKills(ArrayList<InstantData.SecondKill> secondKills) {
        mSecondKills = secondKills;
    }

    public ArrayList<InstantData.SecondKill> getSecondKills() {
        return mSecondKills;
    }
}
