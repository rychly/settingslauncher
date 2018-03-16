package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitiesActivity.onCreateHelper(this, this.getPackageName());
    }

}
