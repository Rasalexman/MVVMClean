package com.mincor.mvvmclean.view.viewholders

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.common.utils.clear
import com.mincor.mvvmclean.common.utils.color
import com.mincor.mvvmclean.common.utils.dip8
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI
import group.infotech.drawable.dsl.*
import org.jetbrains.anko.*

class GenreViewHolder(view: View) : FastAdapter.ViewHolder<GenreUI>(view) {

    private val titleTextView = view.find<TextView>(R.id.title_text_view_id)

    override fun bindView(item: GenreUI, payloads: MutableList<Any>?) {
        titleTextView.text = item.name
    }

    override fun unbindView(item: GenreUI) {
        titleTextView.clear()
    }

    companion object : AnkoComponent<GenreUI> {
        override fun createView(ui: AnkoContext<GenreUI>): View = with(ui) {
            verticalLayout {

                background = stateListDrawable {
                    pressedState {
                        shapeDrawable {
                            solidColor = color(R.color.colorGray20)
                        }
                    }
                    defaultState {
                        shapeDrawable {
                            solidColor = Color.WHITE
                        }
                    }
                }

                lparams(matchParent)

                textView {
                    id = R.id.title_text_view_id
                    textSize = Consts.TEXT_SIZE_MEDIUM
                    textColor = color(R.color.colorGray80)
                    padding = dip8()
                }.lparams {
                    gravity = Gravity.CENTER
                }

                view {
                    backgroundColor = color(R.color.colorGrayLight)
                }.lparams(matchParent, dip(1))
            }
        }
    }
}