package com.github.takahirom.debug.alter.sample;

import com.github.takahirom.library.debug.alter.annotation.DebugReturn;

public class JavaClass {
    @DebugReturn("helloworld")
    public String helloWorld(){
        return "Hello, World!";
    }
}
