package com.devmanishpatole.imagecommenter.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.devmanishpatole.imagecommenter.R
import com.devmanishpatole.imagecommenter.base.BaseActivity
import com.devmanishpatole.imagecommenter.imgur.adapter.ImageListAdapter
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.data.ImageDataState
import com.devmanishpatole.imagecommenter.imgur.viewmodel.ImagesViewModel
import com.devmanishpatole.imagecommenter.ui.CommentActivity.PARCEL_IMAGE
import com.devmanishpatole.imagecommenter.util.getTextChangeFlow
import com.devmanishpatole.imagecommenter.util.hide
import com.devmanishpatole.imagecommenter.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<ImagesViewModel>() {

    override val viewModel: ImagesViewModel by viewModels()

    @Inject
    lateinit var adapter: ImageListAdapter

    override fun getLayoutId() = R.layout.activity_main

    override fun setupView(savedInstanceState: Bundle?) {
        initObserver()
        viewModel.observeSearch(searchBox.getTextChangeFlow())

        imageList.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.column_size))
        imageList.adapter = adapter

        adapter.onItemClick = { image ->
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra(PARCEL_IMAGE, image)
            startActivity(intent)
        }
    }

    private fun initObserver() {
        viewModel.state.observe(this, Observer {
            when (it) {
                is ImageDataState.Success -> handleSuccess(it.list)
                is ImageDataState.ShowProgress -> showProgress()
                is ImageDataState.HideProgress -> progressbar.hide()
                is ImageDataState.NoDataFound -> handleNoData()
                is ImageDataState.NoInternet -> handleNoInternet()
                is ImageDataState.UnknownError -> handleUnknownError()
            }
        })
    }

    private fun handleSuccess(list: List<ImageData>) {
        adapter.replaceData(ArrayList(list))
        imageList.scrollToPosition(0)
        hideKeyboard()
        imageList.show()
    }

    private fun showProgress() {
        imageList.hide()
        errorText.hide()
        progressbar.show()
    }

    private fun handleNoData() {
        progressbar.hide()
        imageList.hide()
        showError(getString(R.string.no_result_found))
    }

    private fun handleNoInternet() {
        progressbar.hide()
        imageList.hide()
        showError(getString(R.string.no_internet))
    }

    private fun handleUnknownError() {
        progressbar.hide()
        imageList.hide()
        showError(getString(R.string.something_went_wrong))
    }

    private fun showError(error: String) {
        errorText.text = error
        errorText.show()
    }
}