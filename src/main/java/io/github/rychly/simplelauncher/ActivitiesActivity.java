package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import io.github.rychly.simplelauncher.items.ActivityItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitiesActivity extends Activity {
    public static final String PACKAGE_NAME_TO_LIST_EXTRAS_KEY = "packageNameToList";

    public static List<ActivityItem> createPackageActivitiesList(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        final List<ActivityItem> activityItems = new ArrayList<>();
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    final String activityLabel = activityInfo.loadLabel(packageManager).toString();
                    final String activityName = activityInfo.name;
                    final ActivityItem activityItem = new ActivityItem(
                            activityName, activityLabel
                            + " (" + ApplicationsActivity.trimActivityInPackageName(packageName, activityName) + ")");
                    activityItems.add(activityItem);
                }
            }
            Collections.sort(activityItems);
        } catch (PackageManager.NameNotFoundException e) {
            // nop
        }
        return activityItems;
    }

    public static void onCreateHelper(Activity activity, String packageNameToList) {
        final LauncherList launcherList = new LauncherList();
        final List<ActivityItem> applicationItems = createPackageActivitiesList(activity, packageNameToList);
        for (ActivityItem activityItem : applicationItems) {
            launcherList.addItem(activityItem.getLabel(), v -> {
                final Intent activityIntent = new Intent();
                activityIntent.setComponent(new ComponentName(activity.getPackageName(), activityItem.getActivityName()));
                activity.startActivity(activityIntent);
            }, null);
        }
        activity.setContentView(launcherList.getView(activity));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String packageNameToList = this.getIntent().getStringExtra(PACKAGE_NAME_TO_LIST_EXTRAS_KEY);
        if (packageNameToList != null) {
            this.setTitle(this.getTitle() + " (" + packageNameToList + ")");
            onCreateHelper(this, packageNameToList);
        } else {
            finish();
        }
    }

}
