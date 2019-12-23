package com.xiling.shared.bean.event;

import com.xiling.module.auth.event.BaseMsg;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/22.
 */
public class MsgMain extends BaseMsg{

    public final static int SELECT_HOME = 1 << 0;

    public MsgMain(int action) {
        super(action);
    }
}
