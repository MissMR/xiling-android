package com.dodomall.ddmall.ddui.custom;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.dodomall.ddmall.ddui.tools.DLog;

import org.xml.sax.XMLReader;

public class TopicTagHandler implements Html.TagHandler {

    public interface TopicTagListener {
        void onTopicTagPressed(String topicId);
    }

    private int startIndex = 0;
    private int stopIndex = 0;

    String topicId = "";

    TopicTagListener listener = null;

    public TopicTagHandler(String topicId, TopicTagListener listener) {
        this.topicId = topicId;
        this.listener = listener;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable editable, XMLReader xmlReader) {
        if (tag.toLowerCase().equals("topic")) {
            if (opening) {
                startReadTag(editable, xmlReader);
            } else {
                endReadTag(editable, xmlReader);
            }
        }
    }

    public void startReadTag(Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }

    public void endReadTag(Editable output, XMLReader xmlReader) {
        stopIndex = output.length();
        output.setSpan(new TopicTagSpan(), startIndex, stopIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private class TopicTagSpan extends ClickableSpan implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onTopicTagPressed(topicId);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //去掉下划线
            ds.setUnderlineText(false);
        }
    }

}
