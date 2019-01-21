package com.example.ednei.gravadorv2;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFilesPath {

    private static String TAG = "ReadsLog";

    public static File[] read() {

        String path = Environment.getExternalStorageDirectory().toString() + "/";
        File dir = new File(path);
        File[] files = dir.listFiles();
        return files;
    }

    public static List<String> getFiles() {

        String path = Environment.getExternalStorageDirectory().toString() + "/";
        File dir = new File(path);
        File[] files = dir.listFiles();
        List<String> list = new ArrayList<>();

        if (files.length > 0) {
            for (File f : files) {
                if (f.getName().contains(".3gp")){
                    list.add(f.getName());
                    Log.e(TAG, f.getName());
                }
            }
        }

        return list;

    }
}
