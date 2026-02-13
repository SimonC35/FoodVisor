package com.foodvisor.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.foodvisor.FoodvisorApplication
import com.foodvisor.databinding.ActivityRestaurantDetailBinding
import com.foodvisor.ui.ViewModelFactory
import com.foodvisor.utils.*

class RestaurantDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESTAURANT_ID = "restaurant_id"
    }

    private lateinit var binding: ActivityRestaurantDetailBinding

    private val viewModel: RestaurantDetailViewModel by viewModels {
        val app = application as FoodvisorApplication
        ViewModelFactory(app.restaurantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
        if (restaurantId == null) {
            showToast("Erreur: Restaurant introuvable")
            finish()
            return
        }

        setupToolbar()
        setupObservers()

        viewModel.loadRestaurant(restaurantId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.restaurant.observe(this) { restaurant ->
            binding.apply {
                // Toolbar
                collapsingToolbar.title = restaurant.nom

                // Image
                ivRestaurant.loadImage(restaurant.featuredImageUrl)

                // Informations
                tvCuisine.text = "Cuisine: ${restaurant.cuisine}"
                tvPrice.text = "Prix moyen: ${restaurant.prixMoyen.formatPrice()}"
                tvRating.text = "Note: ${restaurant.noteGoogle.formatRating()}/5"
                tvStars.text = restaurant.etoilesMichelin.formatStars()
                tvDescription.text = restaurant.description
                tvAddress.text = restaurant.adresse
                tvPhone.text = restaurant.telephone

                // Horaires
                val horairesText = restaurant.horaires?.entries?.joinToString("\n") { (jour, horaire) ->
                    "${jour.replaceFirstChar { it.uppercase() }}: $horaire"
                } ?: "Non disponible"
                tvHoraires.text = horairesText

                // Favori
                fabFavorite.setImageResource(
                    if (restaurant.isFavorite) {
                        android.R.drawable.btn_star_big_on
                    } else {
                        android.R.drawable.btn_star_big_off
                    }
                )

                fabFavorite.setOnClickListener {
                    viewModel.toggleFavorite()
                }

                // Actions
                btnCall.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${restaurant.telephone}")
                    }
                    startActivity(intent)
                }

                btnWebsite.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(restaurant.url)
                    }
                    startActivity(intent)
                }

                btnMap.setOnClickListener {
                    val uri = Uri.parse("geo:${restaurant.latitude},${restaurant.longitude}?q=${restaurant.latitude},${restaurant.longitude}(${restaurant.nom})")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }

                btnShare.setOnClickListener {
                    val shareText = "Découvrez ${restaurant.nom} sur Foodvisor!\n${restaurant.url}"
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    startActivity(Intent.createChooser(shareIntent, "Partager"))
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.show()
                binding.contentLayout.hide()
            } else {
                binding.progressBar.hide()
                binding.contentLayout.show()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }
    }
}