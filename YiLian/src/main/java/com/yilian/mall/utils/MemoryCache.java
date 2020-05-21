package com.yilian.mall.utils;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache {

    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    private long size = 0; // current allocated size
    private long limit = 1000000; // max memory in bytes

    public MemoryCache() {
        // use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
    }

    public Bitmap get(String id) {
        try {
            if (!cache.containsKey(id)) {
                return null;
            }
            return cache.get(id);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public void put(String url, Bitmap bitmap) {
        try {
            if (cache.containsKey(url)) {
                size -= getSizeInBytes(cache.get(url));
            }
            cache.put(url, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        if (size > limit) {
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit) {
                    break;
                }
            }
        }
    }

    public void clear() {
        cache.clear();
    }

    public long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
