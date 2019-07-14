package com.mincor.mvvmclean.view.viewholders

import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.common.utils.*
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI
import org.jetbrains.anko.*

class MovieViewHolder(view: View) : FastAdapter.ViewHolder<MovieUI>(view)  {

    private val imageView: ImageView = view.find(R.id.movie_image_view)
    private val titleTextView: TextView = view.find(R.id.title_text_view)
    private val releaseTextView: TextView = view.find(R.id.release_date_text_view)
    private val overviewTextView: TextView = view.find(R.id.overview_text_view)
    private var progressBar: ProgressBar = view.find(R.id.image_loader)

    override fun bindView(item: MovieUI, payloads: MutableList<Any>) {
        imageView.load(item.fullPosterUrl, progressBar)
        titleTextView.text = item.title
        releaseTextView.text = item.releaseDate
        overviewTextView.text = item.overview
    }

    override fun unbindView(item: MovieUI) {
        //imageView.clear()
    }

    companion object : AnkoComponent<MovieUI> {
        override fun createView(ui: AnkoContext<MovieUI>): View = with(ui) {
            linearLayout {
                val imageWidth = wdthProc(0.4f)
                val height = (imageWidth * 4) / 3
                lparams(matchParent, height)

                relativeLayout {
                    imageView {
                        id = R.id.movie_image_view
                        backgroundColor = color(R.color.colorGray20)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(imageWidth, matchParent)

                    progressBar {
                        id = R.id.image_loader
                        indeterminateDrawable = drawable(R.drawable.spinner_ring)
                        hide(true)
                    }.lparams(dip(24), dip(24)) {
                        centerInParent()
                    }
                }.lparams(wrapContent, matchParent) {
                    margin = dip8()
                }


                verticalLayout {
                    textView {
                        id = R.id.title_text_view
                        textSize = Consts.TEXT_SIZE_LARGE
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = color(R.color.colorPrimaryText)
                    }.lparams(matchParent)

                    textView {
                        id = R.id.release_date_text_view
                        textSize = Consts.TEXT_SIZE_MEDIUM
                        textColor = color(R.color.colorSecondaryText)
                    }.lparams(matchParent)

                    textView {
                        id = R.id.overview_text_view
                        textSize = Consts.TEXT_SIZE_MEDIUM
                        textColor = color(R.color.colorSecondaryText)
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 8
                    }.lparams(matchParent, matchParent) {
                        topMargin = dip8()
                        bottomMargin = dip8()
                    }

                }.lparams(matchParent) {
                    marginEnd = dip8()
                    topMargin = dip8()
                    bottomMargin = dip8()
                }
            }
        }
    }
}