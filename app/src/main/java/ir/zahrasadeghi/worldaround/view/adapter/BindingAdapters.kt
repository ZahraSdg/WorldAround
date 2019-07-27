package ir.zahrasadeghi.worldaround.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.model.RecommendedItem


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("bind:items")
    fun setRecyclerViewItems(recyclerView: RecyclerView, items: PagedList<RecommendedItem>?) {
        (recyclerView.adapter as VenueListAdapter).submitList(items)
    }

    @JvmStatic
    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: String) {

        Picasso.Builder(view.context)
            .build()
            .load(imageUrl)
            .placeholder(R.drawable.ic_image)
            .fit()
            .into(view)
    }
}