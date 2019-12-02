package com.dodomall.ddmall.shared.bean.event;

import com.dodomall.ddmall.module.auth.event.BaseMsg;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/22.
 */
public class MsgCategory extends BaseMsg {
    public final static int ACTION_SHOW_NODATA = 1 << 0;
    public final static int ACTION_GONE_NODATA = 1 << 1;
    public final static int ACTION_RE_QUEST = 1 << 2;

    public MsgCategory(int action) {
        super(action);
    }

}
