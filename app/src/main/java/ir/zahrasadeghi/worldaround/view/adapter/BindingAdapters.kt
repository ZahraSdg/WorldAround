package ir.zahrasadeghi.worldaround.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.data.model.RecommendedItem


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("bind:items")
    fun setRecyclerViewItems(recyclerView: RecyclerView, items: PagedList<RecommendedItem>?) {
        (recyclerView.adapter as VenueListAdapter).submitList(items)
    }

    @JvmStatic
    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {

        if (!imageUrl.isNullOrBlank()) {

            val picasso = Picasso.Builder(view.context).build()

            picasso.load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(view, object : Callback {
                    override fun onSuccess() {
                        picasso.shutdown()
                    }

                    override fun onError(e: Exception?) {
                        loadFromNetwork()
                    }

                    private fun loadFromNetwork() {
                        picasso.load(imageUrl)
                            .placeholder(R.drawable.ic_image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .fit()
                            .into(view, object : Callback {
                                override fun onSuccess() {
                                    picasso.shutdown()
                                }

                                override fun onError(e: Exception?) {
                                    picasso.shutdown()
                                }
                            })
                    }
                })
        }
    }
}