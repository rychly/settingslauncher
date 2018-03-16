package io.github.rychly.simplelauncher.items;

import android.graphics.drawable.Drawable;

public class PackageActivityItem extends ActivityItem {
    private String packageName;
    private Drawable icon;

    public PackageActivityItem(String packageName, String activityName, String label) {
        super(activityName, label);
        this.packageName = packageName;
    }

    public PackageActivityItem(String packageName, String activityName, String label, Drawable icon) {
        this(packageName, activityName, label);
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }
}
