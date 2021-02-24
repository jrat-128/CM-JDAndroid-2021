package com.example.tourlogandroid.ui.gallery.Picture;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PictureContent {

    /**
     * An array of sample (Picture) items.
     */
    public static final List<PictureItem> ITEMS = new ArrayList<PictureItem>();

    /**
     * A map of sample (Picture) items, by ID.
     */
    public static final Map<String, PictureItem> ITEM_MAP = new HashMap<String, PictureItem>();

    public static int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPictureItem(i));
        }
    }

    private static void addItem(PictureItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PictureItem createPictureItem(int position) {
        return new PictureItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A Picture item representing a piece of content.
     */
    public static class PictureItem {
        public final String id;
        public final String content;
        public final String details;

        public PictureItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}