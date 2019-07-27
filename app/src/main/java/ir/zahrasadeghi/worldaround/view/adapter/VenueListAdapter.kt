package ir.zahrasadeghi.worldaround.view.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.databinding.ItemVenueBinding
import ir.zahrasadeghi.worldaround.model.RecommendedItem


class VenueListAdapter : PagedListAdapter<RecommendedItem, VenueListAdapter.ViewHolder>(VenueDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        item?.let { holder.bind(it) }
    }

    class ViewHolder private constructor(private val binding: ItemVenueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendedItem) {

            binding.item = item
            val colorFilter: PorterDuffColorFilter
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colorFilter = PorterDuffColorFilter(
                    binding.root.resources.getColor(
                        R.color.colorAccent, binding.root.resources.newTheme()
                    ), PorterDuff.Mode.SRC_IN
                )
            } else {
                @Suppress("DEPRECATION")
                colorFilter = PorterDuffColorFilter(
                    binding.root.resources.getColor(
                        R.color.colorAccent
                    ), PorterDuff.Mode.SRC_IN
                )
            }

            binding.venueCatIv.colorFilter = colorFilter
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVenueBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class VenueDiffCallback : DiffUtil.ItemCallback<RecommendedItem>() {
    override fun areItemsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem.venue.id === newItem.venue.id
    }

    override fun areContentsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem.venue.name == newItem.venue.name
    }
}