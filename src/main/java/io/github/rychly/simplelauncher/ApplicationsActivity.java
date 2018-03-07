package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationsActivity extends Activity {

    public static View getButton(Context context) {
        return LauncherList.makeButton(context, ApplicationsActivity.class.getCanonicalName(), v -> {
            final Intent activityIntent = new Intent(context, ApplicationsActivity.class);
            context.startActivity(activityIntent);
        });
    }

    private List<ApplicationItem> createApplicationsList(PackageManager pm) {
        final List<ApplicationItem> applicationItems = new ArrayList<>();
        final Intent componentSearchIntent = new Intent();
        componentSearchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        componentSearchIntent.setAction(Intent.ACTION_MAIN);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(componentSearchIntent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (resolveInfo.activityInfo != null) {
                final ApplicationItem applicationItem = new ApplicationItem(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name,
                        (String) (resolveInfo.activityInfo.labelRes == 0
                                ? resolveInfo.loadLabel(pm)
                                : resolveInfo.activityInfo.loadLabel(pm)
                        ));
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
        final List<ApplicationItem> applicationItems = this.createApplicationsList(getPackageManager());
        for (ApplicationItem applicationItem : applicationItems) {
            launcherList.addItem(applicationItem.getLabel(), v -> {
                final Intent applicationIntent = new Intent();
                applicationIntent.setComponent(new ComponentName(applicationItem.getPackageName(), applicationItem.getActivityName()));
                applicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                applicationIntent.setAction(Intent.ACTION_MAIN);
                applicationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(applicationIntent);
            });
        }
        setContentView(launcherList.getView(this));
    }

    class ApplicationItem implements Comparable<ApplicationItem> {
        private String packageName;
        private String activityName;
        private String label;

        ApplicationItem(String packageName, String activityName, String label) {
            this.packageName = packageName;
            this.activityName = activityName;
            this.label = label;
        }

        String getPackageName() {
            return packageName;
        }

        String getActivityName() {
            return activityName;
        }

        String getLabel() {
            return label;
        }

        @Override
        public int compareTo(ApplicationItem applicationItem) {
            if (applicationItem == null) {
                return -1;
            }
            return label.compareToIgnoreCase(applicationItem.label);
        }
    }

}
