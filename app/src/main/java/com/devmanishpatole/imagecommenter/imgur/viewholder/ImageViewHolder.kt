package com.devmanishpatole.imagecommenter.imgur.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleRegistry
import com.devmanishpatole.imagecommenter.R
import com.devmanishpatole.imagecommenter.base.BaseItemViewHolder
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.viewmodel.ImageItemViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.android.synthetic.main.grid_element_layout.view.*

class ImageViewHolder(parent: ViewGroup, private val onItemClick: (Int) -> Unit) :
    BaseItemViewHolder<ImageData, ImageItemViewModel>(R.layout.grid_element_layout, parent) {

    override lateinit var lifecycleRegistry: LifecycleRegistry

    override lateinit var viewModel: ImageItemViewModel

    override fun setupView(view: View) {
        //No Implementation
    }

    override fun bind(data: ImageData) {
        val image = data.images?.firstOrNull()
        image?.let {
            Picasso.get().load(it.imageLink).error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder).fit()
                .into(itemView.imageView)
        } ?: Picasso.get().load(R.drawable.placeholder).fit().into(itemView.imageView)

        itemView.imageTitle.text = data.title

        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    @InstallIn(ActivityComponent::class)
    @EntryPoint
    interface ProviderImageItemViewModel {
        fun viewModel(): ImageItemViewModel
    }

    private fun getViewModel(activity: AppCompatActivity): ImageItemViewModel {
        val hiltEntryPoint = EntryPointAccessors.fromActivity(
            activity,
            ProviderImageItemViewModel::class.java
        )
        return hiltEntryPoint.viewModel()
    }

    override fun injectDependency() {
        lifecycleRegistry = LifecycleRegistry(this)
        viewModel = getViewModel(itemView.context as AppCompatActivity)
    }

}