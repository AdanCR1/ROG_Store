package com.example.rog_store.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val specifications: Map<String, String> = emptyMap(),
    val inStock: Boolean = true,
    val rating: Float = 0.0f,
    val reviewCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
