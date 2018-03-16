package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.os.Bundle;

public class FileManagerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        // check for permission on Android 6.0+ (API Level 23+)
        // https://developer.android.com/training/permissions/requesting.html#perm-request
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        */
    }
}
