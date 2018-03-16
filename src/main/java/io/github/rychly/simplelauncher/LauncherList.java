package io.github.rychly.simplelauncher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

public class LauncherList {
    private static final float BUTTON_PADDING_SCALE = .75f;
    private static final float BUTTON_ICON_SCALE = .5f;
    private List<ListItem> rowItems;

    public LauncherList() {
        this.rowItems = new LinkedList<>();
    }

    public static View makeButton(Context context, String label, View.OnClickListener action, View.OnLongClickListener actionLong, Drawable drawableRight) {
        final Button button = new Button(context);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // align text on the left horizontally and on the center vertically
        button.setGravity(Gravity.CENTER_VERTICAL);
        // decrease the button's padding
        button.setPadding((int) (button.getPaddingLeft() * BUTTON_PADDING_SCALE),
                (int) (button.getPaddingTop() * BUTTON_PADDING_SCALE),
                (int) (button.getPaddingRight() * BUTTON_PADDING_SCALE),
                (int) (button.getPaddingBottom() * BUTTON_PADDING_SCALE));
        // black-in-white text (simple setting bg color will disable the button's shape, so it will be the smallest)
        button.setTextColor(Color.BLACK);
        //button.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        button.setBackgroundColor(Color.WHITE);
        // set label and icon
        button.setText(label);
        if (drawableRight != null) {
            // set button drawable with zero size (so there wont be any space reserved on the button's canvas)
            drawableRight.setBounds(0, 0, 0,
                    (int) (button.getBackground().getIntrinsicHeight() * BUTTON_ICON_SCALE));
            button.setCompoundDrawables(null, null, drawableRight, null);
            // scale drawable when the button size is known (just before drawing the button)
            button.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    button.getViewTreeObserver().removeOnPreDrawListener(this);
                    final float drawableScale = (BUTTON_ICON_SCALE * button.getHeight()) / drawableRight.getIntrinsicHeight();
                    final int newWidth = (int) (drawableRight.getIntrinsicWidth() * drawableScale);
                    final int newHeight = (int) (drawableRight.getIntrinsicHeight() * drawableScale);
                    drawableRight.setBounds(0, 0, newWidth, newHeight);
                    // reserve the space for the drawable
                    button.setPadding(button.getPaddingLeft(), button.getPaddingTop(),
                            button.getPaddingRight() + newWidth, button.getPaddingBottom());
                    return true;
                }
            });
        }
        // set actions
        if (action != null) {
            button.setOnClickListener(action);
        }
        if (actionLong != null) {
            button.setOnLongClickListener(actionLong);
        }
        return button;
    }

    public static View makeButton(Context context, String label, View.OnClickListener action, View.OnLongClickListener actionLong) {
        return LauncherList.makeButton(context, label, action, actionLong, null);
    }

    public ListItem addItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong) {
        final ListItem button = new ListItem(label, action, actionLong);
        this.rowItems.add(button);
        return button;
    }

    public ListItem addItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong, Drawable icon) {
        final ListItem button = new ListItem(label, action, actionLong, icon);
        this.rowItems.add(button);
        return button;
    }

    public View getView(Context context) {
        // layout
        final LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
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
        private View.OnLongClickListener actionLong;
        private Drawable drawable;
        private List<ListItem> buttonItems;

        ListItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong) {
            this.label = label;
            this.action = action;
            this.actionLong = actionLong;
            this.buttonItems = new LinkedList<>();
        }

        ListItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong, Drawable drawable) {
            this(label, action, actionLong);
            this.drawable = drawable;
        }

        ListItem addButtonItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong) {
            final ListItem button = new ListItem(label, action, actionLong);
            this.buttonItems.add(button);
            return button;
        }

        ListItem addButtonItem(String label, View.OnClickListener action, View.OnLongClickListener actionLong, Drawable drawable) {
            final ListItem button = new ListItem(label, action, actionLong, drawable);
            this.buttonItems.add(button);
            return button;
        }

        View getHorizontalView(Context context) {
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

        View getButton(Context context) {
            return LauncherList.makeButton(context, this.label, this.action, this.actionLong, this.drawable);
        }

    }
}
