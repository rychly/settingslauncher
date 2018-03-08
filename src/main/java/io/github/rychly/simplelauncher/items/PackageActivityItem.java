package io.github.rychly.simplelauncher.items;

public class PackageActivityItem implements Comparable<PackageActivityItem> {
    private String packageName;
    private String activityName;
    private String label;

    public PackageActivityItem(String packageName, String activityName, String label) {
        this.packageName = packageName;
        this.activityName = activityName;
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int compareTo(PackageActivityItem applicationItem) {
        if (applicationItem == null) {
            return -1;
        }
        return label.compareToIgnoreCase(applicationItem.label);
    }
}
