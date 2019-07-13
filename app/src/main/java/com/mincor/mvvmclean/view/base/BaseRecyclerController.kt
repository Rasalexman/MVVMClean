package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.common.utils.EndlessRecyclerViewScrollListener
import com.mincor.mvvmclean.common.utils.ScrollPosition
import com.mincor.mvvmclean.common.utils.colorLazy
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by Alex on 07.01.2017.
 * modified at 14.07.2019
 */

abstract class BaseRecyclerController : BaseController {

    constructor()
    constructor(args: Bundle) : super(args)

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    companion object {
        private const val SCROLL_VISIBLE_THRESHOLD = 15
    }

    protected var recycler: RecyclerView? = null
    // current iterating item
    protected var currentItem: AbstractItem<*>? = null

    // layout manager for recycler
    protected open var layoutManager: RecyclerView.LayoutManager? = null
    // направление размещения элементов в адаптере
    protected open val layoutManagerOrientation: Int = LinearLayoutManager.VERTICAL
    // бесконечный слушатель слушатель скролла
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    // custom decorator
    protected open var itemDecoration: RecyclerView.ItemDecoration? = null

    // main adapter items holder
    private val itemAdapter: ItemAdapter<AbstractItem<*>> = ItemAdapter()
    // save our FastAdapter
    protected val mFastItemAdapter:FastAdapter<*> by lazy { FastAdapter.with(itemAdapter) }

    // последняя сохраненная позиция (index & offset) прокрутки ленты
    protected open val previousPosition: ScrollPosition? = ScrollPosition()
    // крутилка прогресса)
    private val progressItem by lazy { ProgressItem().apply { isEnabled = false } }
    // корличесвто элементов до того как пойдет запрос на скролл пагинацию
    protected open val visibleScrollCount get() = SCROLL_VISIBLE_THRESHOLD

    protected open val recyclerBackgroundColor by colorLazy(R.color.colorGrayBack)

    override fun getViewInstance(context: Context): View =
        BaseRecyclerUI().createView(AnkoContext.create(context, this))

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        setRVLayoutManager()     // менеджер лайаута
        setItemDecorator()       // декорации
        setRVCAdapter()          // назначение
        addClickListener()
        addEventHook()           // для нажатия внутри айтемов
    }

    open fun showItems(list: List<AbstractItem<*>>) {
        if (list.isNotEmpty()) {
            itemAdapter.apply {
                clear()
                setNewList(list)
            }
        }
    }

    open fun addNewItems(list: List<AbstractItem<*>>) {
        if (list.isNotEmpty()) {
            itemAdapter.add(0, list)
            scrollToTop()
        }
    }

    open fun addItems(list: List<AbstractItem<*>>) {
        hideLoading()
        if (list.isNotEmpty()) {
            itemAdapter.add(list)
        }
    }

    // менеджер лайаута
    protected open fun setRVLayoutManager() {
        this.activity?.let { context ->
            layoutManager = LinearLayoutManager(context, layoutManagerOrientation, false)
        }
        recycler?.layoutManager = layoutManager
    }

    // промежутки в адаптере
    protected open fun setItemDecorator() {
        if (recycler?.itemDecorationCount == 0) {
            itemDecoration?.let { recycler?.addItemDecoration(it) }
        }
    }

    protected open fun addClickListener() {
        mFastItemAdapter.onClickListener = { _, _, item, position ->
            if(item is AbstractItem<*>) onItemClickHandler(item, position)
            false
        }
    }

    //назначаем адаптеры
    protected open fun setRVCAdapter(isFixedSizes: Boolean = false) {
        recycler?.adapter ?: let {
            recycler?.setHasFixedSize(isFixedSizes)
            recycler?.swapAdapter(mFastItemAdapter, true)
        }
    }

    //
    protected open fun addEventHook() {}

    override fun showError(error: String?) {
        currentItem = null
        hideLoading()
        super.showError(error)
    }

    // слушатель бесконечная прокрутка
    protected fun setRVCScroll() {
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager, visibleScrollCount) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadNextPage(page)
            }
        }
        recycler?.addOnScrollListener(scrollListener!!)
    }

    // Показываем загрузку
    override fun showLoading() {
        hideLoading()
        itemAdapter.add(progressItem)
        isLoading = true
    }

    // прячем загрузку
    override fun hideLoading() {
        val position = itemAdapter.getAdapterPosition(progressItem)
        if (position > -1) itemAdapter.remove(position)
        isLoading = false
    }

    //--------- CALL BACKS FOR RECYCLER VIEW ACTIONS
    protected open fun loadNextPage(page: Int) {}

    protected open fun onItemClickHandler(item: AbstractItem<*>, position: Int) = Unit

    override fun onDetach(view: View) {
        savePreviousPosition()
        hideLoading()
        super.onDetach(view)
    }

    // если хотим сохранить последнюю проскролленную позицию
    protected open fun savePreviousPosition() {
        recycler?.let { rec ->
            previousPosition?.let { scrollPosition ->
                val v = rec.getChildAt(0)
                scrollPosition.index = (rec.layoutManager as? LinearLayoutManager?)?.findFirstVisibleItemPosition() ?: 0
                scrollPosition.top = v?.let { it.top - rec.paddingTop } ?: 0
            }
        }
    }

    // прокручиваем к ранее выбранным элементам
    open fun applyScrollPosition() {
        recycler?.let { rec ->
            stopRecyclerScroll()
            previousPosition?.let {
                (rec.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(it.index, it.top)
            }
        }
    }

    // скролл к верхней записи
    protected fun scrollToTop() {
        stopRecyclerScroll()
        // сбрасываем прокрутку
        previousPosition?.drop()
        // применяем позицию
        applyScrollPosition()
    }

    private fun stopRecyclerScroll() {
        // останавливаем прокрутку
        recycler?.stopScroll()
    }

    private fun clearAdapter() {
        scrollListener?.resetState()
        itemAdapter.clear()
        mFastItemAdapter.notifyAdapterDataSetChanged()
        mFastItemAdapter.notifyDataSetChanged()
    }

    private fun clearFastAdapter() {
        clearAdapter()
        mFastItemAdapter.apply {
            onClickListener = null
            eventHooks.clear()
        }
    }

    private fun clearRecycler() {
        recycler?.apply {
            removeAllViews()
            removeAllViewsInLayout()
            adapter = null
            layoutManager = null
            itemAnimator = null
            clearOnScrollListeners()
            recycledViewPool.clear()

            itemDecoration?.let {
                removeItemDecoration(it)
            }
            itemDecoration = null
        }
    }

    protected fun removeCurrentItemFromAdapter() {
        currentItem?.let {
            itemAdapter.getAdapterPosition(it).let { position ->
                itemAdapter.remove(position)
                currentItem = null
            }
        }
    }

    override fun onDestroyView(view: View) {
        clearFastAdapter()
        clearRecycler()

        layoutManager = null
        scrollListener = null
        itemDecoration = null
        recycler = null
        scrollListener = null
        currentItem = null
        super.onDestroyView(view)
    }

    private class BaseRecyclerUI : AnkoComponent<BaseRecyclerController> {
        override fun createView(ui: AnkoContext<BaseRecyclerController>): View = with(ui) {
            ui.owner.recycler = recyclerView {
                lparams(matchParent, matchParent)
                backgroundColor = ui.owner.recyclerBackgroundColor
            }
            ui.owner.recycler!!
        }
    }
}
