package com.xiling.ddmall.shared.basic;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/2/15.
 */
public class BaseEventMsg {
    private int action;

    public BaseEventMsg(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
