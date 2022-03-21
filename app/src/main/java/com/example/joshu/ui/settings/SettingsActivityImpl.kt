package com.example.joshu.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.example.joshu.R
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.mainScreen.MainScreenActivityImpl
import com.example.joshu.utils.ViewsUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivityImpl : BaseActivityAbs(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    companion object {

        fun show(context: Context) {
            val intent = Intent(context, SettingsActivityImpl::class.java)
            context.startActivity(intent)
        }

        fun show(context: Context, extras: Bundle) {
            val intent = Intent(context, SettingsActivityImpl::class.java)
            intent.putExtras(extras)
            context.startActivity(intent)
        }
    }

    @BindView(R.id.settingsToolbar)
    lateinit var settingsToolbar: Toolbar

    @BindView(R.id.mainSettingsToolbar)
    lateinit var mainSettingsToolbar: Toolbar

    @BindView(R.id.nav_view)
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ButterKnife.bind(this)
        val extras = intent.extras
        if (extras == null || extras.isEmpty) {
            initMainActionBar()
            initBottomNavigation()
            val mainFragment = MainSettings()
            openPreferenceFragment(mainFragment)
        } else {
            val fragment = PreferenceFragmentImpl.getInstance(extras)
            ViewsUtil.goneViews(bottomNavigation)
            openPreferenceFragment(fragment)
            initSettingsActionBar()
        }
    }

    private fun openPreferenceFragment(fragment: PreferenceFragmentCompat) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, fragment)
            .commit()
    }

    override fun initView() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSettingsActionBar() {
        ViewsUtil.goneViews(mainSettingsToolbar)
        ViewsUtil.showViews(settingsToolbar)
        setSupportActionBar(settingsToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initMainActionBar() {
        ViewsUtil.showViews(mainSettingsToolbar)
        ViewsUtil.goneViews(settingsToolbar)
        setSupportActionBar(mainSettingsToolbar)
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?, pref: Preference
    ): Boolean {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment
        )
        fragment.setTargetFragment(caller, 0)
        fragment.arguments = pref.extras
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
        return true
    }

    private fun initBottomNavigation() {
        ViewsUtil.showViews(bottomNavigation)
        bottomNavigation.menu.setGroupCheckable(0, false, true)
        bottomNavigation.setOnNavigationItemSelectedListener {
            MainScreenActivityImpl.show(this, it.itemId)
            true
        }
    }

}