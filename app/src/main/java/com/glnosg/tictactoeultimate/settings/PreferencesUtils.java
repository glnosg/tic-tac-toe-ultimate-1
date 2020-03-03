package com.glnosg.tictactoeultimate.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.glnosg.TicTacToeUltimate;
import com.glnosg.tictactoeultimate.R;

import java.util.Locale;

import androidx.preference.PreferenceManager;

public class PreferencesUtils {

    private static final Context sContext = TicTacToeUltimate.getAppContext();

    public static void updateLocale() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(sContext);
        choseLocale(preferences.getString(sContext.getString(R.string.preferences_language_key), ""));
    }

    private static void choseLocale(String languagePreferenceValue) {
        if (sContext.getString(R.string.preferences_language_value_polish).equals(languagePreferenceValue)) {
            setLocale(new Locale(PreferencesFragment.POLISH));
        } else {
            setLocale(Locale.ENGLISH);
        }
    }

    private static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Resources resources = sContext.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
