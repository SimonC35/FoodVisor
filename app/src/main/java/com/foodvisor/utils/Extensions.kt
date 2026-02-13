package com.foodvisor.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.foodvisor.R

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

// ImageView Extensions
fun ImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_restaurant_placeholder)
        return
    }

    Glide.with(this.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_restaurant_placeholder)
                .error(R.drawable.ic_restaurant_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        )
        .into(this)
}

// Number Extensions
fun Double.formatPrice(): String {
    return String.format("%.0f€", this)
}

fun Double.formatRating(): String {
    return String.format("%.1f", this)
}

fun Int.formatStars(): String {
    return when (this) {
        0 -> "Pas d'étoile"
        1 -> "⭐"
        2 -> "⭐⭐"
        3 -> "⭐⭐⭐"
        else -> "$this étoiles"
    }
}