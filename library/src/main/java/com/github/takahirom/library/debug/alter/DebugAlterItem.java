package com.github.takahirom.library.debug.alter;

/**
 * This class define {@link com.github.takahirom.library.debug.alter.annotation.DebugReturn} method behavior
 * @param <T> Annotated method type
 */
public abstract class DebugAlterItem<T> {
    public final String key;

    protected DebugAlterItem(String key) {
        this.key = key;
    }

    /**
     * If you return true,
     * default {@link com.github.takahirom.library.debug.alter.annotation.DebugReturn} method is not used
     * and {@link DebugAlterItem#get()} is used for method return value.
     */
    public abstract boolean isAlter();

    public abstract T get();
}
