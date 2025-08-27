package com.example.rog_store.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rog_store.data.model.CartItem
import com.example.rog_store.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()
    
    private val _itemCount = MutableStateFlow(0)
    val itemCount: StateFlow<Int> = _itemCount.asStateFlow()
    
    fun addToCart(product: Product, quantity: Int = 1) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex != -1) {
            // Actualizar cantidad si ya existe
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            // Agregar nuevo item
            currentItems.add(CartItem(product, quantity))
        }
        
        _cartItems.value = currentItems
        updateTotals()
    }
    
    fun removeFromCart(productId: String) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.product.id == productId }
        _cartItems.value = currentItems
        updateTotals()
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.product.id == productId }
        
        if (itemIndex != -1) {
            currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = quantity)
            _cartItems.value = currentItems
            updateTotals()
        }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotals()
    }
    
    private fun updateTotals() {
        val items = _cartItems.value
        _total.value = items.sumOf { it.totalPrice }
        _itemCount.value = items.sumOf { it.quantity }
    }
    
    fun getCartItems(): List<CartItem> = _cartItems.value
    
    fun getTotal(): Double = _total.value
}
