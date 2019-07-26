package ir.zahrasadeghi.worldaround.view.adapter

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import ir.zahrasadeghi.worldaround.model.RecommendedItem

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("bind:items")
    fun setRecyclerViewItems(recyclerView: RecyclerView, items: PagedList<RecommendedItem>?) {
        (recyclerView.adapter as VenueListAdapter).submitList(items)
    }
}