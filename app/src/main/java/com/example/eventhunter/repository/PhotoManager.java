package com.example.eventhunter.repository;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class PhotoManager {
    private static PhotoManager instance = null;
    private final Map<String, Bitmap> allBitmaps = new HashMap<>();

    private PhotoManager() {
    }

    public static PhotoManager getInstance() {
        if (instance == null) {
            synchronized (PhotoManager.class) {
                instance = new PhotoManager();
            }
        }
        return instance;
    }

    public void dispose() {
        allBitmaps.clear();
        instance = null;
    }

    public void addBitmap(String key, Bitmap bitmap) {
        if (bitmap != null && !allBitmaps.containsKey(key)) {
            allBitmaps.put(key, bitmap);
        }
    }

    public Bitmap getBitmap(String key) {
        if (allBitmaps.containsKey(key)) {
            return allBitmaps.get(key);
        }
        return null;
    }
}
