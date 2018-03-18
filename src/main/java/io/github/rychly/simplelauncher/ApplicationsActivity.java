package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import io.github.rychly.simplelauncher.items.PackageActivityItem;
import io.github.rychly.simplelauncher.utils.LauncherList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationsActivity extends Activity {

    public static String trimActivityInPackageName(String packageName, String activityName) {
        return activityName.startsWith(packageName) ? activityName.substring(packageName.length()) : activityName;
    }

    public static List<PackageActivityItem> createApplicationsList(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final List<PackageActivityItem> applicationItems = new ArrayList<>();
        // search main activities of all installed packages
        final Intent componentSearchIntent = new Intent();
        componentSearchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        componentSearchIntent.setAction(Intent.ACTION_MAIN);
        final List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(componentSearchIntent, 0);
        // foreach found activity
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (resolveInfo.activityInfo != null) {
                // activity info
                final String activityLabel = (resolveInfo.activityInfo.labelRes == 0
                        ? resolveInfo.loadLabel(packageManager)
                        : resolveInfo.activityInfo.loadLabel(packageManager)).toString();
                final String activityName = resolveInfo.activityInfo.name;
                final String packageName = resolveInfo.activityInfo.packageName;
                final String actInPkgName = trimActivityInPackageName(packageName, activityName);
                Drawable icon = resolveInfo.activityInfo.loadIcon(packageManager);
                // package info
                String versionName;
                try {
                    final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                    versionName = " v" + packageInfo.versionName;
                    if (icon == null) {
                        icon = packageInfo.applicationInfo.loadIcon(packageManager);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    versionName = " noPkg";
                    if (icon == null) {
                        icon = context.getResources().getDrawable(android.R.drawable.btn_default_small);
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
        final List<PackageActivityItem> applicationItems = createApplicationsList(this);
        for (PackageActivityItem applicationItem : applicationItems) {
            launcherList.addItem(applicationItem.getLabel(), v -> {
                final Intent applicationIntent = new Intent();
                applicationIntent.setComponent(new ComponentName(applicationItem.getPackageName(), applicationItem.getActivityName()));
                applicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                applicationIntent.setAction(Intent.ACTION_MAIN);
                applicationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(applicationIntent);
            }, v -> {
                /* // API level 9
                if (android.os.Build.VERSION.SDK_INT >= 9) {
                    final Intent applicationIntent = new Intent();
                    applicationIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    applicationIntent.setData(Uri.fromParts("package", applicationItem.getPackageName(), null));
                    startActivity(applicationIntent);
                    return true;
                }
                */
                final Intent applicationIntent = new Intent(this, ActivitiesActivity.class);
                applicationIntent.putExtra(ActivitiesActivity.PACKAGE_NAME_TO_LIST_EXTRAS_KEY, applicationItem.getPackageName());
                startActivity(applicationIntent);
                return true;
            }, applicationItem.getIcon());
        }
        setContentView(launcherList.getView(this));
    }

}
