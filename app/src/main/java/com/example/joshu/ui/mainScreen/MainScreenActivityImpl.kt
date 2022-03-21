package com.example.joshu.ui.mainScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogFragmentImpl
import com.example.joshu.ui.settings.SettingsActivityImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainScreenActivityImpl :
        BaseActivityAbs(),
        IMainScreenView,
        CreateTaskBottomSheetDialogFragmentImpl.ICreateTaskBottomSheetDialogInteraction {

    companion object {
        private const val ACTION_NEW_TASK = "com.example.joshu.ui.mainScreen.new_task"
        private const val RECORD_AUDIO_REQUEST_CODE = 1
        private const val NAVIGATION_ID = "NAVIGATION_ID"

        fun show(context: Context) {
            val intent = Intent(context, MainScreenActivityImpl::class.java)
            context.startActivity(intent)
        }

        fun show(context: Context, navigationId: Int) {
            val intent = Intent(context, MainScreenActivityImpl::class.java)
            intent.putExtra(NAVIGATION_ID, navigationId)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: MainScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): MainScreenPresenterImpl {
        val presenter = MainScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setVoiceIconToBtnAddTask() {
        addBtnView.setImageResource(R.drawable.ic_voice_24)
    }

    override fun setPlusIconToBtnAddTask() {
        addBtnView.setImageResource(R.drawable.ic_add_24)
    }

    override fun showSpeechListener() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        startActivityForResult(speechRecognizerIntent, RECORD_AUDIO_REQUEST_CODE)
    }

    override fun initActionBar() {
        val toolbar = Toolbar(this)
        setSupportActionBar(toolbar)
    }

    override fun initView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_statistic,
                R.id.navigation_calendar,
                R.id.navigation_list,
                R.id.navigation_today
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        navView.selectedItemId = intent.getIntExtra(NAVIGATION_ID,  R.id.navigation_today)

        if (ACTION_NEW_TASK == intent.action) {
            createNewTask("", false)
        }

        addBtnView.setOnClickListener { view ->
            presenter.onClickAddNewTask()
        }

        addBtnView.performLongClick()
        addBtnView.setOnLongClickListener { view ->
            presenter.onLongClickAddTask()
            true
        }

    }

    override fun createNewTask(text: String?, autoCreate: Boolean) {
        val fragmentTransitionImpl = supportFragmentManager.beginTransaction()
        val newTaskDialog =
            CreateTaskBottomSheetDialogFragmentImpl.createIntent(text, autoCreate)
        newTaskDialog.show(fragmentTransitionImpl, "NewTaskFragment")
        newTaskDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun sendDismissActionToFragment() {
        val navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navFragment?.childFragmentManager?.fragments?.get(0)?.let { itFragment ->
            if (itFragment is IMainScreenCreateDialogTaskInteraction) {
                itFragment.onCreateDialogDismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            parseSpeechText(resultCode, data)
        }
    }

    private fun parseSpeechText(resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val texts = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                presenter.setResultSpeechText(texts)
            }
            Activity.RESULT_CANCELED -> {
                presenter.setCanceledResultSpeechText()
            }
            else -> {
                presenter.setFailedResultSpeechText()
            }
        }
    }

    override fun onDismissCreateTaskBottomSheetDialog() {
        presenter.onDismissCreateDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            SettingsActivityImpl.show(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}