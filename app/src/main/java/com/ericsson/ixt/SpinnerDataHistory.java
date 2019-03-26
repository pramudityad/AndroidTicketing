package com.ericsson.ixt;

/**
 * Created by HPEAdmin on 1/4/2018.
 */

public class SpinnerDataHistory {
    private int icon;
    private String iconName;



    public SpinnerDataHistory(int icon, String iconName) {
        this.icon = icon;
        this.iconName = iconName;
    }

    public int getIcon() {
        return icon;
    }

    public String getIconName() {
        return iconName;
    }
}
