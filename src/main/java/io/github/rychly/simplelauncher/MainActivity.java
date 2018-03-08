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

public class MainActivity extends Activity {

    private List<ActivityItem> createOwnActivityList(Context context) {
        final List<ActivityItem> activityItems = new ArrayList<>();
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    final ActivityItem activityItem = new ActivityItem(
                            activityInfo.name, activityInfo.loadLabel(context.getPackageManager()).toString());
                    activityItems.add(activityItem);
                }
            }
            Collections.sort(activityItems);
        } catch (PackageManager.NameNotFoundException e) {
            // nop
        }
        return activityItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LauncherList launcherList = new LauncherList();
        final List<ActivityItem> applicationItems = this.createOwnActivityList(this);
        for (ActivityItem activityItem : applicationItems) {
            launcherList.addItem(activityItem.getLabel(), v -> {
                final Intent activityIntent = new Intent();
                activityIntent.setComponent(new ComponentName(this.getPackageName(), activityItem.getActivityName()));
                startActivity(activityIntent);
            });
        }
        setContentView(launcherList.getView(this));
    }

}
