package com.xiling.ddui.tools;

import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;

import java.util.HashMap;

public class ShopUtils {

    /**
     *  判断商品状态
     * @param status 商品状态
     * @param store 库存
     */
    public static String  checkShopStatus(int status,int store){
        String result;
        if (status == 1){
            if (store > 0){
                result = "";
            }else{
                result = "已售罄";
            }
        }else{
            result = "已下架";
        }
        return  result;
    }

}
