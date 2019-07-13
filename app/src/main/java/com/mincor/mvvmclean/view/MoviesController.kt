package com.mincor.mvvmclean.view

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.mikepenz.fastadapter.items.AbstractItem
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.utils.pushController
import com.mincor.mvvmclean.common.utils.viewModel
import com.mincor.mvvmclean.view.base.BaseBackRecyclerController
import com.mincor.mvvmclean.viewmodel.MoviesViewModel
import com.mincor.mvvmclean.view.uimodels.movies.MovieUI

open class MoviesController :
    BaseBackRecyclerController {

    constructor()
    constructor(id: Int, genreName: String) : super(bundleOf(KEY_ID to id, KEY_NAME to genreName))

    override val title: String
        get() = args.getString(KEY_NAME).orEmpty()

    private val moviesByGenreViewModel: MoviesViewModel by viewModel()

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        setRVCScroll()
        moviesByGenreViewModel.setGenreId(args.getInt(KEY_ID))
        moviesByGenreViewModel.getStartPage().observe(this, Observer(::handlerObserverResult))
        moviesByGenreViewModel.getNextPage().observe(this, Observer(::handleNextObserverResult))
    }

    override fun onItemClickHandler(item: AbstractItem<*>, position: Int) {
        val movieItem = item as MovieUI
        pushController(MovieDetailsController(movieItem.id))
    }

    override fun loadNextPage(page: Int) {
        moviesByGenreViewModel.loadNext()
    }

    private fun handlerObserverResult(result: SResult<List<MovieUI>>) {
        when(result) {
            is SResult.Loading -> showLoading()
            is SResult.Success -> showItems(result.data)
            is SResult.Error -> showError(result.message)
            else -> hideLoading()
        }
    }

    private fun handleNextObserverResult(result: SResult<List<MovieUI>>) {
        when(result) {
            is SResult.Loading -> showLoading()
            is SResult.Success -> addItems(result.data)
            is SResult.Error -> showError(result.message)
            else -> hideLoading()
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        moviesByGenreViewModel.getStartPage().removeObserver(::handlerObserverResult)
        moviesByGenreViewModel.getNextPage().removeObserver(::handleNextObserverResult)
    }

    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val KEY_NAME = "KEY_NAME"
    }
}