package com.github.takahirom.debug.alter.sample

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView

import com.github.takahirom.library.debug.alter.annotation.DebugReturn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            if (isSnackbarShowTiming()) {
                Snackbar.make(view, getSnackbarText(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }

        val helloWorldText = findViewById<TextView>(R.id.java_hello_world)
        helloWorldText.text = JavaClass().helloWorld()

    }


    @DebugReturn
    fun isSnackbarShowTiming(): Boolean {
        // probably your app contains long logic here
        // ...
        return false
    }

    @DebugReturn
    fun getSnackbarText(): String {
        return "bad"
    }

}
