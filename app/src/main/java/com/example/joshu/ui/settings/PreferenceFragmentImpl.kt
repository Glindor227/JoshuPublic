package com.example.joshu.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.ui.auth.mainScreen.AuthMainScreenActivityImpl
import com.example.joshu.ui.dialog.messageDialog.IMessageDialogInteraction
import com.example.joshu.ui.dialog.messageDialog.MessageDialogFragmentImpl
import com.example.joshu.ui.settings.premium.PremiumDialogFragmentImpl
import com.example.joshu.ui.settings.sync.AuthorizationPreference
import com.example.joshu.ui.settings.sync.IAuthInteraction
import com.example.joshu.ui.settings.tasks.FolderListPreference
import javax.inject.Inject

class PreferenceFragmentImpl : MvpPreferenceFragment(), IPreferenceView, IAuthInteraction {

    companion object {
        private const val PREFERENCE_XML = "PREFERENCE_XML"

        fun getInstance(bundle: Bundle): PreferenceFragmentImpl {
            val fragment = PreferenceFragmentImpl()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: PreferencePresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): PreferencePresenterImpl {
        val presenter = PreferencePresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        if (requireArguments().containsKey(PREFERENCE_XML)) {
            val preferenceXml = resources.getIdentifier(
                requireArguments().getString(PREFERENCE_XML),
                "xml",
                requireContext().packageName
            )
            setPreferencesFromResource(preferenceXml, rootKey)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPreferences()
    }

    private fun initPreferences() {
        initSubscribeCalendar()
        initAddCalendar()
        initDefaultList()
        initPremium()
        initLogout()
        initDeleteAccount()
        setVersionName()
    }

    private fun setVersionName() {
        val packageInfo =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        findPreference<Preference>(R.string.version_key)?.summary =
            packageInfo.versionName
    }

    private fun initDefaultList() {
        if (isPreferenceExist(R.string.default_list_key)) {
            presenter.getDefaultFolder()
        }
        if (isPreferenceExist(R.string.default_list_variants_key)) {
            presenter.loadFoldersInfo()
        }
    }

    override fun updateFolderDefaultList(folders: List<TaskFolder>) {
        val defaultList = findPreference<FolderListPreference>(R.string.default_list_variants_key)
        defaultList?.setEntries(folders)
    }

    override fun updateDefaultTaskListSummary(folderName: String) {
        findPreference<Preference>(R.string.default_list_key)?.summary = folderName
    }

    override fun showAuthAndRegistrationScreen() {
        AuthMainScreenActivityImpl.show(requireContext())
    }

    private fun initSubscribeCalendar() {
        val subscribeCalendarSwitch =
            findPreference<SwitchPreference>(R.string.sync_subscribe_calendar_switch_key)
        subscribeCalendarSwitch?.let {
            it.setOnPreferenceChangeListener { _, value ->
                showSubscribeCalendarButton(value as Boolean)
                true
            }
            showSubscribeCalendarButton(it.isChecked)
        }
    }

    private fun showSubscribeCalendarButton(isVisible: Boolean) {
        val subscribeCalendarButton =
            findPreference<Preference>(R.string.sync_subscribe_calendar_button_key)
        subscribeCalendarButton?.isVisible = isVisible
    }

    private fun initAddCalendar() {
        val addCalendarSwitch =
            findPreference<SwitchPreference>(R.string.sync_add_calendar_switch_key)
        addCalendarSwitch?.let {
            it.setOnPreferenceChangeListener { _, value ->
                showAddCalendarAuthorization(value as Boolean)
                true
            }
            showAddCalendarAuthorization(it.isChecked)
        }
    }

    private fun showAddCalendarAuthorization(isVisible: Boolean) {
        val addCalendarView =
            findPreference<AuthorizationPreference>(R.string.sync_add_calendar_auth_key)
        addCalendarView?.isVisible = isVisible
        addCalendarView?.interaction = this
    }

    private fun initPremium() {
        findPreference<Preference>(R.string.settings_premium_key)?.setOnPreferenceClickListener {
            PremiumDialogFragmentImpl.show(requireContext())
            true
        }
    }

    private fun initLogout() {
        findPreference<Preference>(R.string.account_logout_key)?.setOnPreferenceClickListener {
            requireActivity().supportFragmentManager.let {
                MessageDialogFragmentImpl(strings.logoutMessage,
                    object : IMessageDialogInteraction {
                        override fun onCancel() {
                        }

                        override fun onConfirm() {
                            presenter.logout()
                        }
                    }).show(it, "logoutDialog")
            }
            true
        }
    }

    private fun initDeleteAccount() {
        findPreference<Preference>(R.string.account_delete_key)?.setOnPreferenceClickListener {
            requireActivity().supportFragmentManager.let {
                MessageDialogFragmentImpl(strings.deleteAccountMessage,
                    object : IMessageDialogInteraction {
                        override fun onCancel() {
                        }

                        override fun onConfirm() {
                            presenter.deleteAccount()
                        }
                    }).show(it, "deleteDialog")
            }
            true
        }
    }

    override fun loginWithGoogle() {
        //TODO("Not yet implemented")
    }

    override fun loginWithFacebook() {
        //TODO("Not yet implemented")
    }

    override fun loginWithVK() {
        //TODO("Not yet implemented")
    }

    override fun loginWithEmail() {
        //TODO("Not yet implemented")
    }

    override fun forgotPassword() {
        //TODO("Not yet implemented")
    }
}