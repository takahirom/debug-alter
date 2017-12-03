package com.github.takahirom.library.debug.alter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DebugAlter {

    private static DebugAlter debugAlter;

    public static DebugAlter getInstance() {
        if (debugAlter == null) {
            debugAlter = new DebugAlter();
        }
        return debugAlter;
    }

    private List<DebugAlterItem<?>> debugAlterItems = new ArrayList<>();

    public void setItems(List<DebugAlterItem<?>> items) {
        debugAlterItems.addAll(items);
    }

    boolean alter(@NonNull String key) {
        for (DebugAlterItem<?> debugAlterItem : debugAlterItems) {
            if (key.equals(debugAlterItem.key)) {
                if (debugAlterItem.isAlter()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    Object get(@NonNull String key) {
        for (DebugAlterItem<?> debugAlterItem : debugAlterItems) {
            if (key.equals(debugAlterItem.key)) {
                return debugAlterItem.get();
            }
        }
        return false;
    }
}
