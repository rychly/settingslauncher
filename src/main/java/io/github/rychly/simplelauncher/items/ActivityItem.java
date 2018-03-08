package io.github.rychly.simplelauncher.items;

public class ActivityItem implements Comparable<ActivityItem> {
    private String activityName;
    private String label;

    public ActivityItem(String activityName, String label) {
        this.activityName = activityName;
        this.label = label;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int compareTo(ActivityItem settingItem) {
        if (settingItem == null) {
            return -1;
        }
        return label.compareToIgnoreCase(settingItem.label);
    }
}
