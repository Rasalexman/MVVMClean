package com.mincor.mvvmclean.view.base

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

abstract class BasePagerBackController : BaseBackButtonController(), ViewPager.OnPageChangeListener {

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    private var pagerAdapter: RouterPagerAdapter? = null
    protected var tabLayout: TabLayout? = null
    protected var viewPager: ViewPager? = null
    open var currentPage: Int = 0

    protected open val tabLayoutMode = TabLayout.MODE_FIXED

    // список табов и кол-во
    protected abstract val tabNames: Array<String>

    override fun getViewInstance(context: Context): View = PagerTabUI().createView(AnkoContext.create(context, this))
    //
    protected abstract fun controllerForTab(position: Int): Controller

    override fun onAttach(view: View) {
        super.onAttach(view)
        setupViewPager()
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.currentItem = currentPage
        viewPager?.addOnPageChangeListener(this)
    }

    private fun setupViewPager() {
        pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {
                    router.setRoot(RouterTransaction.with(controllerForTab(position)))
                }
            }

            override fun getCount(): Int = tabNames.size
            override fun getPageTitle(position: Int): CharSequence? = tabNames[position]
        }
        viewPager?.adapter = null
        viewPager?.adapter = pagerAdapter
    }

    override fun onPageScrollStateChanged(p0: Int) {}
    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
    override fun onPageSelected(p0: Int) {
        currentPage = p0
    }

    override fun onDetach(view: View) {
        if (activity?.isChangingConfigurations == false) {
            viewPager?.adapter = null
        }
        tabLayout?.setupWithViewPager(null)
        viewPager?.removeOnPageChangeListener(this)
        super.onDetach(view)
    }

    override fun onDestroy() {
        viewPager?.adapter = null
        viewPager = null
        tabLayout = null
        pagerAdapter = null
        super.onDestroy()
    }


    private class PagerTabUI : AnkoComponent<BasePagerBackController> {
        override fun createView(ui: AnkoContext<BasePagerBackController>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)
                /*appBarLayout {
                    toolBar = toolBar {
                        id = R.id.main_toolbar
                        setTitleTextColor(colorLazy(R.colorLazy.colorWhite))
                    }.lparams(matchParent, dip(56))
                }.lparams(matchParent)*/

                ui.owner.tabLayout = tabLayout {
                    tabMode = ui.owner.tabLayoutMode
                    backgroundColor = Color.WHITE
                }.lparams(matchParent)

                ui.owner.viewPager = viewPager {

                }.lparams(matchParent, matchParent)
            }
        }
    }
}