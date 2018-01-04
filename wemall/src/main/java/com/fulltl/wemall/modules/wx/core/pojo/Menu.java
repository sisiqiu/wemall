package com.fulltl.wemall.modules.wx.core.pojo;

import java.util.List;

/**
 * 菜单pojo类，用来包含Button的pojo类。
 * @author Administrator
 *
 */
public class Menu {
    private List<Button> button;

   
    public List<Button> getButton() {
        return button;
    }

   
    public void setButton(List<Button> button) {
        this.button = button;
    }
}
