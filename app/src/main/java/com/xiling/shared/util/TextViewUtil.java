package com.xiling.shared.util;

import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.shared.bean.Tag;
import com.xiling.shared.component.IconTextSpan;

import java.util.List;

public class TextViewUtil {

    public static void addThroughLine(TextView tv) {
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv.getPaint().setAntiAlias(true);
    }

    public static void removeThroughLine(TextView textView) {
        textView.getPaint().setFlags(0);
    }

    public static void setTagColorTitle(TextView tvTitle, String content, String tag) {
        String str = "<font color='#333333'>" + tag + "</font><font color='#333333'>" + content + "</font>";
        tvTitle.setText(android.text.Html.fromHtml(str));
    }

    public static void setTagTitle(TextView tvTitle, String title, List<Tag> tags) {
        if (tags == null || tags.size() == 0) {
            tvTitle.setText(title);
            LogUtils.e("没有 tag");
        } else {
            LogUtils.e("有 tag");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (Tag tag : tags) {
                spannableStringBuilder.append(createTagSpan(tag.name, ConvertUtil.dip2px(11)));
            }
            if (com.blankj.utilcode.utils.StringUtils.isEmpty(title)) {
                spannableStringBuilder.append(tvTitle.getText().toString());
            } else {
                spannableStringBuilder.append(title);
            }
            tvTitle.setText(spannableStringBuilder);
        }
    }

    private static SpannableStringBuilder createTagSpan(String tag, float textSize) {
        tag = " " + tag + " ";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tag);
        spannableStringBuilder.setSpan(
                new IconTextSpan(MyApplication.getInstance().getApplicationContext(), R.color.text_black, tag),
                spannableStringBuilder.length() - tag.length(),
                spannableStringBuilder.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannableStringBuilder;
    }
}
