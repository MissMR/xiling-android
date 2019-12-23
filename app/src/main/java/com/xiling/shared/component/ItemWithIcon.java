package com.xiling.shared.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.google.common.base.Strings;
import com.xiling.R;
import com.xiling.shared.util.ConvertUtil;


public class ItemWithIcon extends LinearLayout {

    private int iconWidth;
    private int iconHeight;
    private Drawable iconDrawable;
    private int itemBadge;
    private int itemDivider;
    private String itemLabel;
    private TextView labelTv;
    private TextView badgeTv;
    private int itemLabelColor;
    private float itemTextSize;

    public ItemWithIcon(Context context) {
        super(context);
        initViews();
    }

    public ItemWithIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemWithIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemWithIcon);
        iconWidth = typedArray.getDimensionPixelSize(R.styleable.ItemWithIcon_icon_width, ConvertUtil.dip2px(21));
        iconHeight = typedArray.getDimensionPixelSize(R.styleable.ItemWithIcon_icon_height, ConvertUtil.dip2px(21));
        if (typedArray.hasValue(R.styleable.ItemWithIcon_icon_drawable)) {
            iconDrawable = typedArray.getDrawable(R.styleable.ItemWithIcon_icon_drawable);
        }
        itemBadge = typedArray.getInt(R.styleable.ItemWithIcon_item_badge, 0);
        itemDivider = typedArray.getDimensionPixelSize(R.styleable.ItemWithIcon_icon_divider, 20);
        itemLabel = typedArray.getString(R.styleable.ItemWithIcon_item_label);
        itemTextSize = typedArray.getDimension(R.styleable.ItemWithIcon_item_textSize, ConvertUtil.dip2px(10));
        itemTextSize = ConvertUtil.px2dip((int) itemTextSize);
        itemLabel = Strings.isNullOrEmpty(itemLabel) ? "Label" : itemLabel;
        itemLabelColor = typedArray.getColor(R.styleable.ItemWithIcon_item_color, getResources().getColor(R.color.default_text_color));
        typedArray.recycle();

        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cmp_item_with_icon, this);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        labelTv = (TextView) view.findViewById(R.id.itemLabelTv);
        badgeTv = (TextView) view.findViewById(R.id.itemBadgeTv);

        setLabel(itemLabel);
        setLabelColor(itemLabelColor);
        setTextSize(itemTextSize);
        setDivider(itemDivider);
        if (iconDrawable == null) {
            setIcon(R.drawable.default_image, iconWidth, iconHeight);
        } else {
            setIcon(iconDrawable, iconWidth, iconHeight);
        }
        setBadge(itemBadge);
    }

    public void setIcon(@DrawableRes int iconResId, int width, int height) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(iconResId, getResources().newTheme());
        } else {
            drawable = getResources().getDrawable(iconResId);
        }
        setIcon(drawable, width, height);
    }

    public void setIcon(Drawable iconDrawable, int width, int height) {
        iconDrawable.setBounds(0, 0, width, height);
        this.iconDrawable = iconDrawable;
        this.iconHeight = height;
        this.iconWidth = width;
        labelTv.setCompoundDrawables(null, iconDrawable, null, null);
    }

    public void setDivider(int divider) {
        itemDivider = divider;
        labelTv.setCompoundDrawablePadding(divider);
    }

    public void setLabel(CharSequence label) {
        itemLabel = String.valueOf(label);
        labelTv.setText(label);
    }

    public void setTextSize(float dip) {
        itemTextSize = dip;
        labelTv.setTextSize(dip);
    }

    public void setLabelColor(int color) {
        itemLabelColor = color;
        labelTv.setTextColor(color);
    }

    public void setBadge(int badge) {
        itemBadge = badge;
        badgeTv.setVisibility(itemBadge > 0 ? VISIBLE : GONE);
        badgeTv.setText(itemBadge > 99 ? "99+" : "" + itemBadge);

        setBadgeMargins();
    }

    public void setBadgeRedBg(int badge) {
        setBadge(badge);
        setRedBg();
        setBadgeMargins();
    }

    private void setRedBg() {
        badgeTv.setBackgroundResource(R.drawable.cart_badge);
        badgeTv.setTextColor(getResources().getColor(R.color.white));
        badgeTv.setPadding(ConvertUtils.dp2px(3), 0, ConvertUtils.dp2px(3), 0);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) badgeTv.getLayoutParams();
//        layoutParams.setMargins(ConvertUtils.dp2px(30),ConvertUtils.dp2px(10),0,0);

        setBadgeMargins();
    }

    public void setBadge(String text) {
        badgeTv.setVisibility(!StringUtils.isEmpty(text) ? VISIBLE : GONE);
        badgeTv.setText("" + text);
//        setRedBg();
        setBadgeMargins();
    }

    private void setBadgeMargins() {
        if (badgeTv.getText().length() < 3) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) badgeTv.getLayoutParams();
            layoutParams.setMargins(SizeUtils.dp2px(5), 0, 0, 0);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) badgeTv.getLayoutParams();
            layoutParams.setMargins(SizeUtils.dp2px(-8), 0, 0, 0);
        }
    }
}
