package io.github.rychly.simplelauncher;

import android.app.Activity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://github.com/lanceriddle/FileBrowser/blob/master/app/src/main/java/com/filebrowser/MainActivity.java
public class FileManagerActivity extends Activity {

    public static List<File> createFilesList(String path){
        final ArrayList<File> fileItems = new ArrayList<>();
        final File directory = new File(path);
        final File[] files = directory.listFiles();
        fileItems.add(new File(path).getParentFile());
        fileItems.addAll(Arrays.asList(files));
        return fileItems;
    }

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
