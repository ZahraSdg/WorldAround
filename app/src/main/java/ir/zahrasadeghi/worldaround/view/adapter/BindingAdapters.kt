package ir.zahrasadeghi.worldaround.view.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.zahrasadeghi.worldaround.model.RecommendedItem

object BindingAdapters {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @BindingAdapter("bind:items")
    fun setRecyclerViewItems(recyclerView: RecyclerView, items: List<RecommendedItem>?) {
        (recyclerView.adapter as VenueListAdapter).submitList(items)
    }
}