package io.github.rychly.simplelauncher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

public class LauncherList {
    private List<ListItem> rowItems;

    LauncherList() {
        this.rowItems = new LinkedList<>();
    }

    public static View makeButton(Context context, String label, View.OnClickListener action) {
        final Button button = new Button(context);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(label);
        button.setOnClickListener(action);
        return button;
    }

    ListItem addItem(String label, View.OnClickListener action) {
        final ListItem button = new ListItem(label, action);
        this.rowItems.add(button);
        return button;
    }

    public View getActivitiesSwitch(Context context) {
        // layout
        final LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        // buttons
        layout.addView(ApplicationsActivity.getButton(context));
        layout.addView(SettingsActivity.getButton(context));
        return layout;
    }

    public View getView(Context context) {
        // layout
        final LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        // build header content to switch local activities
        layout.addView(this.getActivitiesSwitch(context));
        // build main content
        for (ListItem item : this.rowItems) {
            layout.addView(item.getHorizontalView(context));
        }
        // view
        final ScrollView sc = new ScrollView(context);
        sc.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sc.setFillViewport(true);
        sc.addView(layout);
        return sc;
    }

    class ListItem {
        private String label;
        private View.OnClickListener action;
        private List<ListItem> buttonItems;

        ListItem(String label, View.OnClickListener action) {
            this.label = label;
            this.action = action;
            this.buttonItems = new LinkedList<>();
        }

        ListItem addButtonItem(String label, View.OnClickListener action) {
            final ListItem button = new ListItem(label, action);
            this.buttonItems.add(button);
            return button;
        }

        public View getHorizontalView(Context context) {
            // layout
            final LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            // main button
            layout.addView(this.getButton(context));
            // other buttonItems
            for (ListItem item : this.buttonItems) {
                layout.addView(item.getButton(context));
            }
            return layout;
        }

        public View getButton(Context context) {
            return LauncherList.makeButton(context, this.label, this.action);
        }

    }
}
