package com.xiling.ddui.tools;

import android.widget.TextView;

/**
 * 对数字进行处理
 */
public class NumberHandler {

    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static String reservedDecimalFor2(double number){
       return String.format("%.2f", number);
    }

    /**
     * 将整数和小数部分，分别赋值给不同的textView
     */
    public static void  setPriceText(double number, TextView integerText,TextView decimalView){
        String numStr = reservedDecimalFor2(number);
        float fNum = Float.parseFloat(numStr);
        integerText.setText(((int)fNum)+".");
        decimalView.setText(numStr.substring(numStr.length() -2,numStr.length()));
    }


}
