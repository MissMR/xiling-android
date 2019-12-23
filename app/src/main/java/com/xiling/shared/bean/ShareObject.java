package com.xiling.shared.bean;

import com.xiling.shared.util.BitmapUtil;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-06
 */
public class ShareObject {
    public String title;
    public String desc;
    public String url;
    public String thumb;


    public byte[] getThumbBytes() {
        return BitmapUtil.bitmapToBytes(BitmapUtil.scaleBitmapForShare(thumb));
    }
}
