package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import io.github.rychly.simplelauncher.items.PackageActivityItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationsActivity extends Activity {

    private List<PackageActivityItem> createApplicationsList(PackageManager pm) {
        final List<PackageActivityItem> applicationItems = new ArrayList<>();
        // search main activities of all installed packages
        final Intent componentSearchIntent = new Intent();
        componentSearchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        componentSearchIntent.setAction(Intent.ACTION_MAIN);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(componentSearchIntent, 0);
        // foreach found activity
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (resolveInfo.activityInfo != null) {
                // activity info
                final String activityLabel = (resolveInfo.activityInfo.labelRes == 0
                        ? resolveInfo.loadLabel(pm)
                        : resolveInfo.activityInfo.loadLabel(pm)).toString();
                final String activityName = resolveInfo.activityInfo.name;
                final String packageName = resolveInfo.activityInfo.packageName;
                final String actInPkgName = activityName.startsWith(packageName)
                        ? activityName.substring(packageName.length())
                        : activityName;
                Drawable icon = resolveInfo.activityInfo.loadIcon(pm);
                // package info
                String versionName;
                try {
                    final PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                    versionName = " v" + packageInfo.versionName;
                    if (icon == null) {
                        icon = packageInfo.applicationInfo.loadIcon(pm);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    versionName = " noPkg";
                    if (icon == null) {
                        icon = this.getResources().getDrawable(android.R.drawable.btn_default_small);
                    }
                }
                //
                final PackageActivityItem applicationItem = new PackageActivityItem(packageName, activityName,
                        activityLabel + versionName + " (" + packageName + "/" + actInPkgName + ")", icon);
                applicationItems.add(applicationItem);
            }
        }
        Collections.sort(applicationItems);
        return applicationItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LauncherList launcherList = new LauncherList();
        final List<PackageActivityItem> applicationItems = this.createApplicationsList(getPackageManager());
        for (PackageActivityItem applicationItem : applicationItems) {
            launcherList.addItem(applicationItem.getLabel(), v -> {
                final Intent applicationIntent = new Intent();
                applicationIntent.setComponent(new ComponentName(applicationItem.getPackageName(), applicationItem.getActivityName()));
                applicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                applicationIntent.setAction(Intent.ACTION_MAIN);
                applicationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(applicationIntent);
            }, applicationItem.getIcon());
        }
        setContentView(launcherList.getView(this));
    }

}
