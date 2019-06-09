package com.mincor.mvvmclean.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.mincor.mvvmclean.R
import com.mincor.mvvmclean.common.consts.Consts
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.utils.*
import com.mincor.mvvmclean.domain.model.local.MovieEntity
import com.mincor.mvvmclean.view.base.BaseBackButtonController
import com.mincor.mvvmclean.viewmodel.MovieDetailViewModel
import org.jetbrains.anko.*

class MovieDetailsController : BaseBackButtonController {

    constructor()
    constructor(movie_id: Int) : super(bundleOf(KEY_MOVIE_ID to movie_id))

    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override val title: String
        get() = string(R.string.title_overview)

    override fun getViewInstance(context: Context): View {
        return createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        movieDetailViewModel.setMovieId(args.getInt(KEY_MOVIE_ID))
        movieDetailViewModel.getMovieDetails().observe(this, Observer(::movieDetailsHandler))
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        movieDetailViewModel.getMovieDetails().removeObserver(::movieDetailsHandler)
    }

    private fun movieDetailsHandler(result: SResult<MovieEntity>) {
        when (result) {
            is SResult.Loading -> showLoading()
            is SResult.Success -> updateUI(result.data)
            is SResult.Error -> showError(result.message)
        }
    }

    private fun updateUI(movieEntity: MovieEntity) {
        hideLoading()
        view?.apply {
            val mainLayout: RelativeLayout = find(R.id.movie_detail_view)
            mainLayout.show()

            find<ImageView>(R.id.backdrop_image_view).load(movieEntity.getBackDropImageUrl())
            find<ImageView>(R.id.movie_image_view).load(movieEntity.getImageUrl())
            find<TextView>(R.id.title_text_view).text = movieEntity.title
            find<TextView>(R.id.release_date_text_view).text = Consts.UI_DATE_FORMATTER.format(movieEntity.releaseDate)
            find<TextView>(R.id.overview_text_view).text = movieEntity.overview
        }
    }

    companion object : AnkoComponent<MovieDetailsController> {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        override fun createView(ui: AnkoContext<MovieDetailsController>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)
                backgroundColor = color(R.color.colorGray20)

                scrollView {
                    lparams(matchParent, matchParent)

                    relativeLayout {
                        visible = false
                        id = R.id.movie_detail_view

                        frameLayout {
                            id = R.id.backdrop_view

                            imageView {
                                id = R.id.backdrop_image_view
                                backgroundColor = color(R.color.colorGray20)
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }.lparams(matchParent, matchParent)

                            view {
                                backgroundColor = color(R.color.colorBackdrop)
                            }.lparams(matchParent, matchParent)

                        }.lparams(matchParent, hdthProc(0.4f)) {
                            alignParentTop()
                        }


                        linearLayout {

                            val wdth = wdthProc(0.45f)
                            val hdth = (wdth * 4) / 3

                            relativeLayout {
                                imageView {
                                    id = R.id.movie_image_view
                                    backgroundColor = Color.WHITE
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                }.lparams(wdth, matchParent)

                                progressBar {
                                    id = R.id.image_loader
                                    indeterminateDrawable = drawable(R.drawable.spinner_ring)
                                    hide(true)
                                }.lparams(dip(24), dip(24)) {
                                    centerInParent()
                                }
                            }.lparams(wrapContent, hdth) {
                                margin = dip16()
                            }

                            verticalLayout {
                                textView {
                                    id = R.id.title_text_view
                                    textSize = Consts.TEXT_SIZE_XLARGE
                                    typeface = Typeface.DEFAULT_BOLD
                                    textColor = Color.WHITE
                                }.lparams(matchParent)

                                textView {
                                    id = R.id.release_date_text_view
                                    textSize = Consts.TEXT_SIZE_MEDIUM
                                    textColor = Color.WHITE
                                }.lparams(matchParent)

                            }.lparams(matchParent) {
                                marginEnd = dip8()
                                topMargin = dip8()
                                bottomMargin = dip8()
                            }

                        }.lparams(matchParent)

                        textView {
                            id = R.id.overview_text_view
                            textSize = Consts.TEXT_SIZE_MEDIUM
                            textColor = color(R.color.colorPrimaryText)
                            ellipsize = TextUtils.TruncateAt.END
                        }.lparams(matchParent, wrapContent) {
                            margin = dip8()
                            below(R.id.backdrop_view)
                        }
                    }.lparams(matchParent)
                }
            }
        }
    }
}