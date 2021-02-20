package com.plweegie.android.squashtwo.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.plweegie.android.squashtwo.R;

public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PREF_SORT_BY_SETTING = "pref_sortBySetting";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
