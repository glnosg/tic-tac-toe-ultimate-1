package com.glnosg.tictactoeultimate.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glnosg.tictactoeultimate.R;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String POLISH = "pl";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_app);
        setLanguageListPreferenceSummary();
    }

    private void setLanguageListPreferenceSummary() {
        ListPreference preference = findPreference(getString(R.string.preferences_language_key));
        Locale currentLocale = getResources().getConfiguration().locale;
        if (preference != null) {
            if (currentLocale.getLanguage().equals(POLISH)) {
                preference.setSummary(getString(R.string.preferences_language_summary_polish));
            } else {
                preference.setSummary(getString(R.string.preferences_language_summary_english));
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_language_key))) {
            String value =
                    sharedPreferences
                            .getString(key, getString(R.string.preferences_language_value_english));
            choseLocale(value);
            setLanguageListPreferenceSummary();
        }
    }

    private void choseLocale(String languagePreferenceValue) {
        if (getString(R.string.preferences_language_value_polish).equals(languagePreferenceValue)) {
            setLocale(new Locale(POLISH));
        } else {
            setLocale(Locale.ENGLISH);
        }
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.colorBoard));
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
