package io.github.rychly.simplelauncher.utils;

import java.io.File;
import java.util.Comparator;

/**
 * Compare files by their full path in the directory tree.
 * https://stackoverflow.com/questions/4440765/comparatorfile-for-directories-first-order#4441853
 */
public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File first, File second) {
        if (first.isDirectory() && second.isDirectory())
            return first.compareTo(second);
        if (first.isDirectory())
            return this.compareToFile(first, second);
        if (second.isDirectory())
            return -(this.compareToFile(second, first));
        return this.compareFiles(first, second);
    }

    private int compareFiles(File first, File second) {
        final File firstParentFile = first.getParentFile();
        final File secondParentFile = second.getParentFile();
        if (isSubDir(firstParentFile, secondParentFile))
            return -1;
        if (isSubDir(secondParentFile, firstParentFile))
            return 1;
        return first.compareTo(second);
    }

    private int compareToFile(File directory, File file) {
        final File fileParent = file.getParentFile();
        if (directory.equals(fileParent))
            return -1;
        if (isSubDir(directory, fileParent))
            return -1;
        return directory.compareTo(file);
    }

    private boolean isSubDir(File directory, File subDir) {
        for (File parentDir = directory.getParentFile(); parentDir != null; parentDir = parentDir.getParentFile()) {
            if (subDir.equals(parentDir))
                return true;
        }
        return false;
    }
}