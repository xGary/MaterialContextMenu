package com.devgary.materialcontextmenu.materialcontextmenu;

/**
 * Created by Gary on 2016-09-10.
 */

public class MenuItem {

    private String string;
    private int icon;

    public MenuItem(String string) {
        this.string = string;
    }

    public MenuItem(String string, int icon) {
        this.string = string;
        this.icon = icon;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
