package com.example.joshu.di.component

import com.example.joshu.di.module.*
import com.example.joshu.ui.auth.emailScreen.AuthEmailScreenActivityImpl
import com.example.joshu.ui.auth.emailScreen.AuthEmailScreenPresenterImpl
import com.example.joshu.ui.auth.mainScreen.AuthMainScreenActivityImpl
import com.example.joshu.ui.auth.mainScreen.AuthMainScreenPresenterImpl
import com.example.joshu.ui.auth.regEmailScreen.RegEmailScreenActivityImpl
import com.example.joshu.ui.auth.regEmailScreen.RegEmailScreenPresenterImpl
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.CreateFolderBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.CreateFolderBottomSheetDialogPresenterImpl
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogPresenterImpl
import com.example.joshu.ui.dialog.dateTimeChoicer.DateTimeChoicerScreenFragmentImpl
import com.example.joshu.ui.dialog.dateTimeChoicer.DateTimeChoicerScreenPresenterImpl
import com.example.joshu.ui.dialog.folderSelected.FolderSelectedBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.folderSelected.FolderSelectedBottomSheetDialogPresenterImpl
import com.example.joshu.ui.dialog.messageDialog.MessageDialogFragmentImpl
import com.example.joshu.ui.forgotPasswordByEmailScreen.ForgotPasswordScreenActivityImpl
import com.example.joshu.ui.forgotPasswordByEmailScreen.ForgotPasswordScreenPresenterImpl
import com.example.joshu.ui.mainScreen.MainScreenActivityImpl
import com.example.joshu.ui.mainScreen.MainScreenPresenterImpl
import com.example.joshu.ui.mainScreen.calendar.CalendarScreenFragmentImpl
import com.example.joshu.ui.mainScreen.calendar.CalendarScreenPresenterImpl
import com.example.joshu.ui.mainScreen.list.ListScreenFragmentImpl
import com.example.joshu.ui.mainScreen.list.ListScreenPresenterImpl
import com.example.joshu.ui.mainScreen.list.folder.FolderScreenFragmentImpl
import com.example.joshu.ui.mainScreen.list.folder.FolderScreenPresenterImpl
import com.example.joshu.ui.mainScreen.statistic.StatisticScreenFragmentImpl
import com.example.joshu.ui.mainScreen.statistic.StatisticScreenPresenterImpl
import com.example.joshu.ui.mainScreen.today.TodayScreenFragmentImpl
import com.example.joshu.ui.mainScreen.today.TodayScreenPresenterImpl
import com.example.joshu.ui.settings.PreferenceFragmentImpl
import com.example.joshu.ui.settings.PreferencePresenterImpl
import com.example.joshu.ui.splashScreen.SplashScreenActivityImpl
import com.example.joshu.ui.splashScreen.SplashScreenPresenterImpl
import com.example.joshu.ui.widget.TaskListRemoteViewsFactory
import com.example.joshu.ui.widget.TaskListWidgetProvider
import com.example.joshu.utils.BufferTaskUtilImpl
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class, RetrofitModule::class, RepoModule::class, UtilsModule::class,
        DBModule::class]
)
@Singleton
interface AppComponent {
    fun inject(activity: SplashScreenActivityImpl)
    fun inject(activity: AuthMainScreenActivityImpl)
    fun inject(activity: ForgotPasswordScreenActivityImpl)
    fun inject(activity: AuthEmailScreenActivityImpl)
    fun inject(activity: RegEmailScreenActivityImpl)

    fun inject(activity: MainScreenActivityImpl)

    fun inject(fragment: StatisticScreenFragmentImpl)
    fun inject(fragment: CalendarScreenFragmentImpl)
    fun inject(fragment: ListScreenFragmentImpl)
    fun inject(fragment: TodayScreenFragmentImpl)
    fun inject(fragment: DateTimeChoicerScreenFragmentImpl)
    fun inject(fragment: CreateTaskBottomSheetDialogFragmentImpl)
    fun inject(fragment: FolderSelectedBottomSheetDialogFragmentImpl)
    fun inject(fragment: CreateFolderBottomSheetDialogFragmentImpl)
    fun inject(fragment: MessageDialogFragmentImpl)
    fun inject(presenter: FolderScreenFragmentImpl)
    fun inject(presenter: PreferenceFragmentImpl)

    fun inject(presenter: SplashScreenPresenterImpl)
    fun inject(presenter: AuthMainScreenPresenterImpl)
    fun inject(presenter: ForgotPasswordScreenPresenterImpl)
    fun inject(presenter: AuthEmailScreenPresenterImpl)
    fun inject(presenter: RegEmailScreenPresenterImpl)
    fun inject(presenter: MainScreenPresenterImpl)
    fun inject(presenter: StatisticScreenPresenterImpl)
    fun inject(presenter: CalendarScreenPresenterImpl)
    fun inject(presenter: ListScreenPresenterImpl)
    fun inject(presenter: TodayScreenPresenterImpl)
    fun inject(presenter: DateTimeChoicerScreenPresenterImpl)
    fun inject(presenter: CreateTaskBottomSheetDialogPresenterImpl)
    fun inject(presenter: CreateFolderBottomSheetDialogPresenterImpl)
    fun inject(presenter: FolderSelectedBottomSheetDialogPresenterImpl)
    fun inject(presenter: FolderScreenPresenterImpl)
    fun inject(presenter: PreferencePresenterImpl)

    fun inject(util: BufferTaskUtilImpl)

    fun inject(factory: TaskListRemoteViewsFactory)
    fun inject(widgetProvider: TaskListWidgetProvider)
}