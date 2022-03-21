package com.example.joshu.ui.mainScreen.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.customView.FilterMenuItem
import com.example.joshu.customView.MenuViewGroup
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenFragmentImpl
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenPresenter
import com.example.joshu.viewUtils.StatusUiUtils
import kotlinx.android.synthetic.main.toolbar.*

class TodayScreenFragmentImpl : BaseTaskScreenFragmentImpl(), ITodayScreenView {

    @InjectPresenter
    lateinit var presenter: TodayScreenPresenterImpl

    lateinit var filter: MenuViewGroup

    private val listOfMenuChip = arrayListOf<FilterMenuItem>()

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): TodayScreenPresenterImpl {
        val presenter = TodayScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun getPresenter(): IBaseTaskScreenPresenter = presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today_screen, container, false)
        ButterKnife.bind(view)
        filter = view.findViewById(R.id.filter)
        filter.setSingleSelection(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TaskStatusEnum.values().forEach {
            if (StatusUiUtils.getVisible(it)) {
                filter.add(createChipView(it))
            }
        }
    }

    override fun initActionBar(text: String) {
        setToolBar(toolbarView)

        getActionBar()?.apply {
            title = text
        }
    }

    override fun updateMenuItems() {
        val hashMap = presenter.getMenuItemScorePresenter()
        listOfMenuChip.forEach {
            hashMap[it.index]?.let { it1 -> it.setScore(it1) }
        }
    }

    private fun createChipView(statusEnum: TaskStatusEnum): FilterMenuItem {
        val chip = FilterMenuItem(requireContext())
        chip.setTitle(resources.getText(StatusUiUtils.getName(statusEnum)).toString())
        chip.setIcon(StatusUiUtils.getIcon(statusEnum))
        chip.index = statusEnum.value
        chip.setOnItemClickListener(object : FilterMenuItem.OnClickListener {
            override fun onClick(view: View) {
                swipeHelper.clearSwipe()
                presenter.selectedStatusTask(chip.isChecked, statusEnum)
            }
        })
        listOfMenuChip.add(chip)
        return chip
    }
}