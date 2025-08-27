package com.example.rog_store.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val product: Product,
    val quantity: Int = 1
) : Parcelable {
    val totalPrice: Double
        get() = product.price * quantity
}
