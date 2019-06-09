package com.mincor.mvvmclean.view.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.listeners.OnClickListener
import com.mikepenz.fastadapter_extensions.items.ProgressItem
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.common.utils.EndlessRecyclerViewScrollListener
import com.mincor.mvvmclean.common.utils.ScrollPosition
import com.mincor.mvvmclean.common.utils.colorLazy
import com.mincor.mvvmclean.common.utils.log
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by Alex on 07.01.2017.
 */

abstract class BaseRecyclerController : BaseController, OnClickListener<AbstractItem<*, *>> {

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
    protected var currentItem: AbstractItem<*, *>? = null

    // layout manager for recycler
    protected open var layoutManager: RecyclerView.LayoutManager? = null
    // направление размещения элементов в адаптере
    protected open val layoutManagerOrientation: Int = LinearLayoutManager.VERTICAL
    // бесконечный слушатель слушатель скролла
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    // custom decorator
    protected open var itemDecoration: RecyclerView.ItemDecoration? = null
    // save our FastAdapter
    protected var mFastItemAdapter: FastItemAdapter<AbstractItem<*, *>>? = null
    // последняя сохраненная позиция (index & offset) прокрутки ленты
    protected open val previousPosition: ScrollPosition? = ScrollPosition()
    // крутилка прогресса)
    private val progressItem = ProgressItem().withEnabled(false)
    // корличесвто элементов до того как пойдет запрос на скролл пагинацию
    protected open val visibleScrollCount get() = SCROLL_VISIBLE_THRESHOLD

    protected open val recyclerBackgroundColor by colorLazy(R.color.colorGrayBack)

    override fun getViewInstance(context: Context): View =
        BaseRecyclerUI().createView(AnkoContext.create(context, this))

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        setRVLayoutManager()     // менеджер лайаута
        setItemDecorator()       // декорации
        createAdapter()          // адаптер
        setRVCAdapter()          // назначение
        addEventHook()           // для нажатия внутри айтемов
    }

    open fun showItems(list: List<AbstractItem<*, *>>) {
        if (list.isNotEmpty()) {
            mFastItemAdapter?.apply {
                clear()
                setNewList(list)
            }
        }
    }

    open fun addNewItems(list: List<AbstractItem<*, *>>) {
        if (list.isNotEmpty()) {
            mFastItemAdapter?.add(0, list)
            scrollToTop()
        }
    }

    open fun addItems(list: List<AbstractItem<*, *>>) {
        hideLoading()
        if (list.isNotEmpty()) {
            mFastItemAdapter?.add(list)
        }
    }

    // менеджер лайаута
    protected open fun setRVLayoutManager() {
        layoutManager ?: this.activity?.let { context ->
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

    // создаем адаптеры
    private fun createAdapter() {
        mFastItemAdapter ?: let {
            mFastItemAdapter = FastItemAdapter()
            addClickListenerToAdapter()
        }
    }

    protected open fun addClickListenerToAdapter() {
        mFastItemAdapter?.withOnClickListener(this)
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
        scrollListener = scrollListener ?: object : EndlessRecyclerViewScrollListener(layoutManager, visibleScrollCount) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadNextPage(page)
            }
        }
        recycler?.addOnScrollListener(scrollListener!!)
    }

    // Показываем загрузку
    override fun showLoading() {
        hideLoading()
        mFastItemAdapter?.add(progressItem)
        isLoading = true
    }

    // прячем загрузку
    override fun hideLoading() {
        val position = mFastItemAdapter?.getAdapterPosition(progressItem) ?: -1
        if (position > -1) mFastItemAdapter?.remove(position)
        isLoading = false
    }

    //--------- CALL BACKS FOR RECYCLER VIEW ACTIONS
    protected open fun loadNextPage(page: Int) {}

    override fun onClick(
        v: View?,
        adapter: IAdapter<AbstractItem<*, *>>?,
        item: AbstractItem<*, *>,
        position: Int
    ): Boolean {
        log { "ITEM CLICKED ON POSITION $position" }
        onItemClickHandler(item, position)
        return false
    }

    protected open fun onItemClickHandler(item: AbstractItem<*, *>, position: Int) {}

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

    fun clearAdapter() {
        scrollListener?.resetState()
        mFastItemAdapter?.apply {
            clear()
            notifyAdapterDataSetChanged()
        }
    }

    private fun clearFastAdapter() {
        mFastItemAdapter?.apply {
            withOnClickListener(null)
            eventHooks?.clear()
            clear()
            notifyDataSetChanged()
            notifyAdapterDataSetChanged()
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
        mFastItemAdapter?.getAdapterPosition(currentItem)?.let {
            mFastItemAdapter?.remove(it)
            currentItem = null
        }
    }

    override fun onDestroyView(view: View) {
        clearFastAdapter()
        clearRecycler()

        mFastItemAdapter = null
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
