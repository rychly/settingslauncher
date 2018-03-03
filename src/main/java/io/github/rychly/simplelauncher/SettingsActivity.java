package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;

public class SettingsActivity extends Activity {

    private static final String NAME_PREFIX = "ACTION_";
    private static final String NAME_SUFFIX = "_SETTINGS";
    private static final String NAME_MAIN = "ACTION_SETTINGS";

    private View settingMenuButton(final String action, final String title) {
        final LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        final Button button = new Button(this);
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        button.setText(title);
        row.addView(button);
        button.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(action);
            // NOT USABLE FOR SETTINGS INTENTS
            // it will be considered the HOME activity and will be on top of the stack as needed for the system to broadcast BOOT_COMPLETED
            //settingsIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(settingsIntent);
        });
        return row;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        // https://developer.android.com/reference/android/provider/Settings.html
        Field[] fields = Settings.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final String name = field.getName();
            if (name.startsWith(NAME_PREFIX) && name.endsWith(NAME_SUFFIX)) {
                try {
                    final String title = NAME_MAIN.equals(name) ? name
                            : name.substring(NAME_PREFIX.length(), name.length() - NAME_SUFFIX.length());
                    final String value = (String) field.get(null);
                    layout.addView(settingMenuButton(value, title));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    // nop
                }
            }
        }

        final ScrollView sc = new ScrollView(this);
        sc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        sc.setFillViewport(true);
        sc.addView(layout);

        setContentView(sc);
    }
}
