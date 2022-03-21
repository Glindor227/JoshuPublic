package com.example.joshu.ui.settings

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arellomobile.mvp.MvpDelegate
import com.example.joshu.mvp.model.Constants
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil

//MvpAppCompatFragment but for preferences

abstract class MvpPreferenceFragment : PreferenceFragmentCompat(), IBaseView {
    private var lockScreen: ViewGroup? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Constants.SHARED_PREFERENCES
    }

    private var mIsStateSaved = false

    lateinit var mMvpDelegate: MvpDelegate<out MvpPreferenceFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate().onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onStart() {
        super.onStart()
        mIsStateSaved = false
        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()
        mIsStateSaved = false
        getMvpDelegate().onAttach()
        updatePreferenceTitle()
    }

    private fun updatePreferenceTitle() {
        if (activity is AppCompatActivity)
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = preferenceScreen.title
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mIsStateSaved = true
        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()
        getMvpDelegate().onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (requireActivity().isFinishing) {
            getMvpDelegate().onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }
        if (isRemoving || anyParentIsRemoving) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Fragment.
     */
    fun getMvpDelegate(): MvpDelegate<*> {
        if (!this::mMvpDelegate.isInitialized) {
            mMvpDelegate = MvpDelegate<MvpPreferenceFragment>(this)
        }
        return mMvpDelegate
    }

    override fun initView() {
    }

    override fun closeScreen() {
        activity?.finish()
    }

    override fun closeAffinityScreen() {
        activity?.finishAffinity()
    }

    override fun lockScreen() {
        ViewsUtil.showViews(lockScreen)
    }

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(view)
    }

    override fun unlockScreen() {
        ViewsUtil.goneViews(lockScreen)
    }

    override fun showError(errorMessage: CharSequence) {
        showToast(errorMessage)
    }

    override fun showMessage(message: CharSequence) {
        showToast(message)
    }

    fun <T : Preference?> findPreference(@StringRes preferenceKey: Int): T? {
        return findPreference<T>(getString(preferenceKey))
    }

    fun isPreferenceExist(@StringRes preferenceKey: Int): Boolean {
        return findPreference<Preference>(getString(preferenceKey)) != null
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}