package com.github.takahirom.debug.alter.sample

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class PrefsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}