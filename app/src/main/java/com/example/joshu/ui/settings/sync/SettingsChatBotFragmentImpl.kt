package com.example.joshu.ui.settings.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import com.example.joshu.R
import com.example.joshu.ui.BaseFragmentAbs

class SettingsChatBotFragmentImpl : BaseFragmentAbs(), ISettingsChatBotView {

    @BindView(R.id.addChatBot)
    lateinit var addChatBotButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_chat_bot, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updatePreferenceTitle()
    }

    private fun updatePreferenceTitle() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.sync_add_bot_title)
        }
    }

    override fun addChatBot() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        TODO("Not yet implemented")
    }
}