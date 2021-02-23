package com.plweegie.android.squashtwo.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.plweegie.android.squashtwo.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_PREF_SORT_BY_SETTING = "pref_sortBySetting";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
