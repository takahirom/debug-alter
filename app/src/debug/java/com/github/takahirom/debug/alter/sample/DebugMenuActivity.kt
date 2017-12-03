package com.github.takahirom.debug.alter.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

class DebugMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, PrefsFragment())
                .commit()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, DebugMenuActivity::class.java)
        }
    }
}
