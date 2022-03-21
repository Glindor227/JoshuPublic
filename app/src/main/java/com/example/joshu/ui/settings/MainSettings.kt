package com.example.joshu.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import com.example.joshu.R
import com.example.joshu.ui.settings.premium.PremiumDialogFragmentImpl

class MainSettings : MvpPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.prefs_main, rootKey)
        initPreferences()
    }

    private fun initPreferences() {
        setOnPreferenceClickListener(getString(R.string.settings_account_key))
        setOnPreferenceClickListener(getString(R.string.settings_tasks_key))
        setOnPreferenceClickListener(getString(R.string.app_settings_key))
        setOnPreferenceClickListener(getString(R.string.settings_about_key))
        initPremium()
    }

    private fun setOnPreferenceClickListener(preferenceKey: String) {
        findPreference<Preference>(preferenceKey)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                SettingsActivityImpl.show(requireContext(), it.extras)
                true
            }
    }

    private fun initPremium() {
        findPreference<Preference>(R.string.settings_premium_key)?.setOnPreferenceClickListener {
            PremiumDialogFragmentImpl.show(requireContext())
            true
        }
    }

}