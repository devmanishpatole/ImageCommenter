package com.devmanishpatole.imagecommenter.imgur.adapter

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.devmanishpatole.imagecommenter.base.BaseAdapter
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.viewholder.ImageViewHolder

class ImageListAdapter(parentLifecycle: Lifecycle, dataList: ArrayList<ImageData>) :
    BaseAdapter<ImageData, ImageViewHolder>(parentLifecycle, dataList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageViewHolder(parent, ::onClick)

    lateinit var onItemClick: (ImageData) -> Unit

    private fun onClick(position: Int) {
        if (::onItemClick.isInitialized) {
            onItemClick(dataList[position])
        }
    }
}