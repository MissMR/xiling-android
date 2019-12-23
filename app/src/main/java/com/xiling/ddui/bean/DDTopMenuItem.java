package com.xiling.ddui.bean;

import com.xiling.ddui.custom.DDTopMenuPopupWindow;

/**
 * created by Jigsaw at 2018/12/20
 */
public class DDTopMenuItem extends DDTopMenuPopupWindow.MenuItemText {
    private String itemText;

    public DDTopMenuItem(String itemText) {
        this.itemText = itemText;
    }

    @Override
    public String getMenuItemText() {
        return itemText;
    }
}
