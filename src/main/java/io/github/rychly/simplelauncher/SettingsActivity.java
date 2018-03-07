package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsActivity extends Activity {

    private static final String NAME_PREFIX = "ACTION_";
    private static final String NAME_SUFFIX = "_SETTINGS";
    private static final String NAME_MAIN = "ACTION_SETTINGS";

    public static View getButton(Context context) {
        return LauncherList.makeButton(context, SettingsActivity.class.getCanonicalName(), v -> {
            final Intent activityIntent = new Intent(context, SettingsActivity.class);
            context.startActivity(activityIntent);
        });
    }

    private List<SettingItem> createSettingsList() {
        final List<SettingItem> settingsItems = new ArrayList<>();
        // https://developer.android.com/reference/android/provider/Settings.html
        final Field[] fields = Settings.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final String name = field.getName();
            if (name.startsWith(NAME_PREFIX) && name.endsWith(NAME_SUFFIX)) {
                try {
                    final String label = NAME_MAIN.equals(name) ? name
                            : name.substring(NAME_PREFIX.length(), name.length() - NAME_SUFFIX.length());
                    final String activityName = (String) field.get(null);
                    final SettingItem settingItem = new SettingItem(activityName, label);
                    settingsItems.add(settingItem);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    // nop
                }
            }
        }
        Collections.sort(settingsItems);
        return settingsItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LauncherList launcherList = new LauncherList();
        final List<SettingItem> settingItems = this.createSettingsList();
        for (SettingItem settingItem : settingItems) {
            launcherList.addItem(settingItem.getLabel(), v -> {
                final Intent settingsIntent = new Intent(settingItem.getActivityName());
                // NOT USABLE FOR SETTINGS INTENTS
                // it will be considered the HOME activity and will be on top of the stack as needed for the system to broadcast BOOT_COMPLETED
                //settingsIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(settingsIntent);
            });
        }
        setContentView(launcherList.getView(this));
    }

    class SettingItem implements Comparable<SettingItem> {
        private String activityName;
        private String label;

        SettingItem(String activityName, String label) {
            this.activityName = activityName;
            this.label = label;
        }

        String getActivityName() {
            return activityName;
        }

        String getLabel() {
            return label;
        }

        @Override
        public int compareTo(SettingItem settingItem) {
            if (settingItem == null) {
                return -1;
            }
            return label.compareToIgnoreCase(settingItem.label);
        }
    }
}
