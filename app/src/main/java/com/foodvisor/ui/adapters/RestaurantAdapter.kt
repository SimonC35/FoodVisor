package com.foodvisor.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foodvisor.data.model.Restaurant
import com.foodvisor.databinding.ItemRestaurantBinding
import com.foodvisor.utils.*

class RestaurantAdapter(
    private val onItemClick: (Restaurant) -> Unit,
    private val onFavoriteClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RestaurantViewHolder(
        private val binding: ItemRestaurantBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            binding.apply {
                tvRestaurantName.text = restaurant.nom
                tvCuisine.text = restaurant.cuisine
                tvPrice.text = restaurant.prixMoyen.formatPrice()
                tvRating.text = restaurant.noteGoogle.formatRating()
                tvStars.text = restaurant.etoilesMichelin.formatStars()
                tvAddress.text = restaurant.adresse

                // Image
                ivRestaurant.loadImage(restaurant.featuredImageUrl)

                // Icône favori
                btnFavorite.setImageResource(
                    if (restaurant.isFavorite) {
                        android.R.drawable.btn_star_big_on
                    } else {
                        android.R.drawable.btn_star_big_off
                    }
                )

                // Clicks
                root.setOnClickListener { onItemClick(restaurant) }
                btnFavorite.setOnClickListener { onFavoriteClick(restaurant) }
            }
        }
    }
}

class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }
}