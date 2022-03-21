package com.example.joshu.ui.mainScreen.calendar

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.CalendarDate
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.observable.IObservableTask
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenPresenterImpl
import com.example.joshu.utils.ComparatorTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.max
import kotlin.math.min

@InjectViewState
class CalendarScreenPresenterImpl(private val strings: IStrings)
    : BaseTaskScreenPresenterImpl<ICalendarScreenView>(strings), ICalendarScreenPresenter {

    companion object {
        private const val SHIFT_MONTH_CALENDAR_VIEW = 1
    }

    @Inject
    lateinit var observableTask: IObservableTask

    private var listTasks: MutableMap<CalendarDate, MutableList<Task>> = mutableMapOf()

    private val cacheTaskFolder = HashMap<Int, TaskFolder>()
    private val onGetTaskFolderById: (Int) -> TaskFolder? = {
        cacheTaskFolder[it]
    }
    private var selectedDate: CalendarDate
    private var minDate = Long.MAX_VALUE
    private var maxDate = 0L

    class DataDecoration {
        val listForDecoratorFourTask = HashSet<CalendarDate>()

        var listForDecoratorOnePriority = HashMap<PriorityTypeEnum, HashSet<CalendarDate>>()
        var listForDecoratorTwoPriority = HashMap<HashSet<PriorityTypeEnum>, HashSet<CalendarDate>>()
        var listForDecoratorThreePriority = HashMap<HashSet<PriorityTypeEnum>, HashSet<CalendarDate>>()

        fun clear() {
            listForDecoratorFourTask.clear()
        }
    }

    init {
        listPresenter = TaskListPresenterImpl(
                onGetTaskFolderById, onClick, onClickExpand,
                dateTimeFormatter
        )

        val calendar = Calendar.getInstance()

        selectedDate = CalendarDate(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + SHIFT_MONTH_CALENDAR_VIEW,
                calendar.get(Calendar.YEAR))
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getTasksListByDB()
        viewState.setSelectedDate(selectedDate)
        onDateSelected(selectedDate)
        observableTask.subscribeToNewTask(this)
    }

    override fun onDestroy() {
        observableTask.unSubscribeToNewTask(this)
    }

    private fun getTasksListByDB() {
        val job = scope.launch {
            val allTasks = dbTaskDao.getAllByDeleteStatus(DeleteStatusEnum.No.value)
            val dataDecoration = prepareTaskList(allTasks)

            withContext(Dispatchers.Main) {
                viewState.setMinMaxDateToCalendar(
                    getDateFromMilliseconds(minDate),
                    getDateFromMilliseconds(maxDate))
                viewState.createDecoration(dataDecoration)
                onDateSelected(selectedDate)
            }
        }
        addJob(job, "getTasksListByDB")
    }

    private fun prepareTaskList(listTask: List<Task>): DataDecoration {
        val resultData = DataDecoration()
        val listForDecoratorOneTask = HashMap<CalendarDate, PriorityTypeEnum>()
        val listForDecoratorTwoTask = HashMap<CalendarDate, HashSet<PriorityTypeEnum>>()
        val listForDecoratorThreeTask = HashMap<CalendarDate, HashSet<PriorityTypeEnum>>()

        listTasks.clear()

        listTask.forEach {
            val dateKey = getDateFromMilliseconds(it.dateTime)
            val priority = PriorityTypeEnum.getByValue(it.priority)

            val list = listTasks[dateKey] ?: run {
                val newList = mutableListOf<Task>()
                listTasks[dateKey] = newList
                newList
            }
            list.add(it)

            checkForMinAndMaxDate(it)

            when {
                resultData.listForDecoratorFourTask.contains(dateKey) ->
                    fillListForDecoratorFourTasks(resultData.listForDecoratorFourTask, dateKey)
                listForDecoratorThreeTask.contains(dateKey) ->
                    fillListForDecoratorThreeTasks(
                            listForDecoratorThreeTask,
                            resultData.listForDecoratorFourTask,
                            dateKey, priority, priority, emptySet())
                listForDecoratorTwoTask.contains(dateKey) ->
                    fillListForDecoratorTwoPriority(
                            listForDecoratorTwoTask,
                            listForDecoratorThreeTask,
                            resultData.listForDecoratorFourTask,
                            dateKey, priority, priority)
                else ->
                    fillListForDecoratorOnePriority(
                            listForDecoratorOneTask,
                            listForDecoratorTwoTask,
                            listForDecoratorThreeTask,
                            resultData.listForDecoratorFourTask,
                            dateKey,
                            PriorityTypeEnum.getByValue(it.priority))
            }
        }

        checkForMinAndMaxDateWithCurrentTime()

        resultData.listForDecoratorOnePriority = groupDatesForPriorities(listForDecoratorOneTask)
        resultData.listForDecoratorTwoPriority = groupDatesForPriorities(listForDecoratorTwoTask)
        resultData.listForDecoratorThreePriority = groupDatesForPriorities(listForDecoratorThreeTask)

        return resultData
    }

    private fun checkForMinAndMaxDate(task: Task) {
        minDate = min(minDate, task.dateTime)
        maxDate = max(maxDate, task.dateTime)
    }

    private fun checkForMinAndMaxDateWithCurrentTime() {
        val currentTime = System.currentTimeMillis()
        minDate = min(minDate, currentTime)
        maxDate = max(maxDate, currentTime)
    }

    private fun <E> groupDatesForPriorities(
            list: HashMap<CalendarDate, E>):
            HashMap<E, HashSet<CalendarDate>> {
        val result = HashMap<E, HashSet<CalendarDate>>()

        list.forEach {
            result[it.value]?.add(it.key) ?: run { result[it.value] = hashSetOf(it.key) }
        }

        return result
    }

    private fun fillListForDecoratorOnePriority(listForDecoratorOneTask: HashMap<CalendarDate, PriorityTypeEnum>,
                                                listForDecoratorTwoTask: HashMap<CalendarDate, HashSet<PriorityTypeEnum>>,
                                                listForDecoratorThreeTask: HashMap<CalendarDate, HashSet<PriorityTypeEnum>>,
                                                listForDecoratorFourTask: HashSet<CalendarDate>,
                                                dateKey: CalendarDate,
                                                priority: PriorityTypeEnum) {

        listForDecoratorOneTask[dateKey]?.let {
            if (it != priority) {
                fillListForDecoratorTwoPriority(listForDecoratorTwoTask,
                        listForDecoratorThreeTask,
                        listForDecoratorFourTask,
                        dateKey, it, priority)
                listForDecoratorOneTask.remove(dateKey)
            }
        } ?: run { listForDecoratorOneTask[dateKey] = priority }
    }

    private fun fillListForDecoratorTwoPriority(listForDecoratorTwoTask: HashMap<CalendarDate, HashSet<PriorityTypeEnum>>,
                                                listForDecoratorThreeTask: HashMap<CalendarDate, HashSet<PriorityTypeEnum>>,
                                                listForDecoratorFourTask: HashSet<CalendarDate>,
                                                dateKey: CalendarDate,
                                                priorityFirst: PriorityTypeEnum,
                                                prioritySecond: PriorityTypeEnum) {
        listForDecoratorTwoTask[dateKey]?.let {
            if (!it.contains(priorityFirst) || !it.contains(prioritySecond)) {
                fillListForDecoratorThreeTasks(
                        listForDecoratorThreeTask,
                        listForDecoratorFourTask,
                        dateKey,
                        priorityFirst,
                        prioritySecond,
                        it)
                listForDecoratorTwoTask.remove(dateKey)
            }
        } ?: run {
            listForDecoratorTwoTask[dateKey] = HashSet(setOf(priorityFirst, prioritySecond))
        }
    }

    private fun fillListForDecoratorThreeTasks(listForDecoratorThreeTask: HashMap<CalendarDate, HashSet<PriorityTypeEnum>>,
                                               listForDecoratorFourTask: HashSet<CalendarDate>,
                                               dateKey: CalendarDate,
                                               priorityFirst: PriorityTypeEnum,
                                               prioritySecond: PriorityTypeEnum,
                                               priorityThirdAndFourth: Set<PriorityTypeEnum>) {
        listForDecoratorThreeTask[dateKey]?.let {
            if (!it.contains(priorityFirst) || !it.contains(prioritySecond) ||
                    !it.containsAll(priorityThirdAndFourth)) {
                fillListForDecoratorFourTasks(listForDecoratorFourTask, dateKey)
                listForDecoratorThreeTask.remove(dateKey)
            }
        } ?: run {
            val values = HashSet(priorityThirdAndFourth)
            values.add(priorityFirst)
            values.add(prioritySecond)
            if (values.size > 3) {
                fillListForDecoratorFourTasks(listForDecoratorFourTask, dateKey)
                listForDecoratorThreeTask.remove(dateKey)
            } else {
                listForDecoratorThreeTask[dateKey] = values
            }
        }
    }

    private fun fillListForDecoratorFourTasks(listForDecoratorFourTask: HashSet<CalendarDate>,
                                              dateKey: CalendarDate) {
        listForDecoratorFourTask.add(dateKey)
    }

    private fun getDateFromMilliseconds(time: Long): CalendarDate {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return CalendarDate(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR))
    }

    override fun getTaskList() {
        val job = scope.launch {
            var tasks = mutableListOf<Task>()
            listTasks[selectedDate]?.let { tasks = it }
            val taskList = ComparatorTask.sortTasksByDate(tasks)

            listPresenter.clear()
            listPresenter.list.addAll(taskList)

            updateCacheTaskFolder(taskList.map { it.folderId })

            withContext(Dispatchers.Main) {
                viewState.updateList()
                viewState.enableEmptyView(listPresenter.list.isEmpty())
            }
        }
        addJob(job, "getTaskList")
    }

    override fun initActionBar() {
        setActionBarTodayTitle()
    }

    override fun updateTask(task: Task) {
        val job = scope.launch {
            dbTaskDao.update(task)

            withContext(Dispatchers.Main) {
                viewState.updateList()
            }
        }
        addJob(job, "updateDataToDB")
    }

    override fun onDateSelected(date: CalendarDate) {
        val currentYear = getDateFromMilliseconds(System.currentTimeMillis()).year
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month - 1, date.day)

        when {
            calendar.timeInMillis == System.currentTimeMillis() ->
                setActionBarTodayTitle()
            currentYear != date.year ->
                viewState.initActionBar(dateTimeFormatter.getDateLongOfCalendar(calendar))
            else ->
                viewState.initActionBar(dateTimeFormatter.getDayMonthFull(calendar.timeInMillis))
        }

        selectedDate = date

        getTaskList()
    }

    private fun setActionBarTodayTitle() {
        val nowDate = dateTimeFormatter.getDayMonthFull(System.currentTimeMillis())
        viewState.initActionBar(strings.nowPrefix(nowDate))
    }

    override fun onAppBarCollapsed() {
        viewState.onAppBarCollapsed()
    }

    override fun onAppBarExpanded() {
        viewState.onAppBarExpanded()
    }

    override fun onCreatedOrUpdatedTask(task: Task) {
        getTasksListByDB()
    }

    private fun updateCacheTaskFolder(ids: List<Int>) {
        cacheTaskFolder.clear()

        if (ids.isNotEmpty()) {
            val job = scope.launch {
                dbFolderDao.getByFilteredId(ids).forEach {
                    cacheTaskFolder[it.id] = it
                }
            }
            addJob(job, "updateCacheTaskFolder")
        }
    }
}