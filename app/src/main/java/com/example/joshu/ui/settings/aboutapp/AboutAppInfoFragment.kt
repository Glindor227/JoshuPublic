package com.example.joshu.ui.settings.aboutapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.joshu.R
import com.example.joshu.ui.BaseFragmentAbs
import kotlinx.android.synthetic.main.fragment_settings_info.*

class AboutAppInfoFragment : BaseFragmentAbs() {

    companion object {
        private const val TITLE = "TITLE"
        private const val CONTENT = "CONTENT"

        fun getInstance(fragmentTitle: String, content: String): AboutAppInfoFragment {
            val bundle = Bundle()
            bundle.putString(TITLE, fragmentTitle)
            bundle.putString(CONTENT, content)
            val fragment = AboutAppInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentTitle = requireArguments().getString(TITLE)
        val contentName = requireArguments().getString(CONTENT)
        updatePreferenceTitle(fragmentTitle)
        infoText.text =
            HtmlCompat.fromHtml(getInfoText(contentName), HtmlCompat.FROM_HTML_MODE_LEGACY)
        infoText.movementMethod = LinkMovementMethod()
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    private fun updatePreferenceTitle(fragmentTitle: String?) {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = fragmentTitle
        }
    }

    private fun getInfoText(contentFileName: String?): String {
        if (contentFileName == null) {
            return ""
        }
        val contentFileId =
            resources.getIdentifier(contentFileName, "raw", requireContext().packageName)
        val inputStream = resources.openRawResource(contentFileId)
        return inputStream.bufferedReader().use { it.readText() }
    }
}