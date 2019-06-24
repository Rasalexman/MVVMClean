package com.mincor.mvvmclean.view.base

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.activity.ActionBarProvider
import com.mincor.mvvmclean.common.utils.childrenVisible
import com.mincor.mvvmclean.common.utils.clear
import com.mincor.mvvmclean.common.utils.colorLazy
import com.mincor.mvvmclean.common.utils.gradientBg
import com.rasalexman.kdispatcher.IKDispatcher
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.progressBar
import org.jetbrains.anko.toast

abstract class BaseController : ViewBindController, IKDispatcher {

    constructor()
    constructor(args: Bundle) : super(args)

    protected var toolBar: Toolbar? = null
    protected var progressBar: ProgressBar? = null

    // идет ли загрузка
    protected var isLoading: Boolean = false
    protected open val title: String = ""
    protected open val toolbarTitleColor = Color.WHITE
    protected open val progressBarColor by colorLazy(R.color.colorProgressBar)
    protected open val toolbarBackgroundColor by colorLazy(R.color.colorPrimary)

    protected open val topBackgroundColor by colorLazy(R.color.colorLightGray)
    protected open val bottomBackgroundColor by colorLazy(R.color.colorPrimaryDark)
    protected open val backgroundDrawable: Drawable
        get() = gradientBg(arrayOf(topBackgroundColor, bottomBackgroundColor))

    override fun onViewCreated(view: View) {
        attachListeners()
    }

    override fun onDestroyView(view: View) {
        toolBar = null
        hideLoading()
        detachListeners()
        clearAllView()
        super.onDestroyView(view)
    }

    protected open fun createToolBar() {
        if(toolBar == null && activity != null) {
            toolBar = Toolbar(activity).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                backgroundColor = toolbarBackgroundColor
                setTitleTextColor(toolbarTitleColor)
            }
            val actionBar =
                AppBarLayout(activity).apply {
                    layoutParams = AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT)
                    backgroundColor = toolbarBackgroundColor
                    this.addView(toolBar)
            }

            when(view) {
                is ScrollView -> {
                    ((view as ScrollView).getChildAt(0) as? ViewGroup)?.addView(actionBar, 0)
                }
                is CoordinatorLayout -> {
                    val params = toolBar?.layoutParams as? AppBarLayout.LayoutParams
                    params?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    (view as CoordinatorLayout).addView(actionBar, 0)
                }
                else -> (view as? ViewGroup)?.addView(actionBar, 0)
            }
        }
    }

    open fun clearAllView() {
        (view as? ViewGroup)?.clear()
    }

    open fun showError(error: String?) {
        error?.let {
            activity?.toast(error)
        }
    }

    protected fun closeApp() {
        activity?.finish()
    }

    open fun showLoading() {
        (view as? ViewGroup)?.let {
            it.childrenVisible(false)
            with(it) {
                progressBar = progressBar {
                    layoutParams = when(it) {
                        is LinearLayout -> {
                            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                                gravity = Gravity.CENTER
                            }
                        }
                        is RelativeLayout -> {
                            RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                                centerInParent()
                            }
                        }
                        else -> ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    }
                }

                progressBar?.indeterminateDrawable?.setColorFilter(
                    progressBarColor,
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
        isLoading = true
    }

    open fun hideLoading() {
        (view as? ViewGroup)?.apply {
            progressBar?.let {
                this.removeView(it)
            }
            this.childrenVisible(true)
        }
        isLoading = false
    }

    //handle the click on the back arrow click
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            goBack()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    protected open fun goBack() {
        router.popController(this)
    }

    /**
     * Открываем внешнюю ссылку в браузере без задержки
     */
    fun openBrowserByUrl(url:String?){
        if(!url.isNullOrEmpty()){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

    /**
     * Назначаем слушателей для текущего Контроллера
     */
    protected open fun attachListeners() {}

    /**
     * Удаляем слушателей для текущего контроллера
     */
    protected open fun detachListeners() {}


    /***
     * Все что касается хранения и обработки тулбара
     */
    protected val actionBar: ActionBar?
        get() {
            val actionBarProvider = activity as ActionBarProvider?
            return actionBarProvider?.getSupportActionBar()
        }

    protected fun setActionBar() {
        toolBar?.let {
            (activity as? ActionBarProvider)?.setSupportActionBar(it)
        }
    }

    protected fun activateOptionsMenu() {
        super.setHasOptionsMenu(true)
    }

    protected fun setTitle() {
        toolBar?.title = title
    }
}
