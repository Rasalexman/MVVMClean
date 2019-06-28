package com.mincor.mvvmclean.view

import android.view.View
import androidx.lifecycle.Observer
import com.mikepenz.fastadapter.items.AbstractItem
import com.mincor.mvvmclean.common.dto.SResult
import com.mincor.mvvmclean.common.utils.pushController
import com.mincor.mvvmclean.common.utils.viewModel
import com.mincor.mvvmclean.view.base.BaseRecyclerController
import com.mincor.mvvmclean.view.uimodels.genres.GenreUI
import com.mincor.mvvmclean.viewmodel.GenresViewModel

class GenresController : BaseRecyclerController() {

    private val categoriesViewModel: GenresViewModel by viewModel()

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        categoriesViewModel.getDataList().observe (this, Observer(::handlerObserverResult))
    }

    private fun handlerObserverResult(result: SResult<List<GenreUI>>) {
        when(result) {
            is SResult.Loading -> showLoading()
            is SResult.Success -> showItems(result.data)
            is SResult.Error -> showError(result.message)
        }
    }

    override fun onItemClickHandler(item: AbstractItem<*, *>, position: Int) {
        val genreItem = (item as GenreUI)
        pushController(MoviesController(genreItem.id, genreItem.name))
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        categoriesViewModel.getDataList().removeObserver(::handlerObserverResult)
    }
}
