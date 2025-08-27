package com.example.rog_store.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rog_store.R
import com.example.rog_store.data.model.CartItem
import com.example.rog_store.ui.theme.*
import com.example.rog_store.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total by cartViewModel.total.collectAsState()
    val itemCount by cartViewModel.itemCount.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ROGBlack, ROGDarkGray)
                )
            )
    ) {
        // Header
        CartHeader(
            onBackClick = { navController.navigateUp() },
            itemCount = itemCount
        )
        
        if (cartItems.isEmpty()) {
            EmptyCart(navController = navController)
        } else {
            // Lista de productos
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onQuantityIncrease = { cartViewModel.updateQuantity(cartItem.product.id, cartItem.quantity + 1) },
                        onQuantityDecrease = { cartViewModel.updateQuantity(cartItem.product.id, cartItem.quantity - 1) },
                        onRemove = { cartViewModel.removeFromCart(cartItem.product.id) }
                    )
                }
            }
            
            // Resumen del carrito
            CartSummary(
                total = total,
                onCheckout = {
                    // TODO: Implementar checkout
                }
            )
        }
    }
}

@Composable
fun CartHeader(
    onBackClick: () -> Unit,
    itemCount: Int
) {
    TopAppBar(
        title = { 
            Text(
                text = "Carrito de Compras",
                color = ROGWhite
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = ROGWhite
                )
            }
        },
        actions = {
            Badge(
                containerColor = ROGRed
            ) {
                Text(
                    text = itemCount.toString(),
                    color = ROGWhite
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ROGDarkGray
        )
    )
}

@Composable
fun EmptyCart(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = ROGGray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Tu carrito está vacío",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Agrega algunos productos ROG para comenzar",
            fontSize = 16.sp,
            color = ROGGray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(
                containerColor = ROGRed
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Explorar Productos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ROGDarkGray
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen del producto
            AsyncImage(
                model = cartItem.product.imageUrl.ifEmpty { R.drawable.placeholder_product },
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ROGWhite
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$${cartItem.product.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ROGRed
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onQuantityDecrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Reducir cantidad",
                            tint = ROGRed
                        )
                    }
                    
                    Text(
                        text = cartItem.quantity.toString(),
                        color = ROGWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    IconButton(
                        onClick = onQuantityIncrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            tint = ROGRed
                        )
                    }
                }
            }
            
            // Botón eliminar
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar del carrito",
                    tint = ROGNeonPink
                )
            }
        }
        
        // Subtotal del item
        Divider(
            color = ROGGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subtotal:",
                color = ROGGray,
                fontSize = 14.sp
            )
            Text(
                text = "$${cartItem.totalPrice}",
                color = ROGWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CartSummary(
    total: Double,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ROGRed
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    color = ROGWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", total)}",
                    color = ROGWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ROGWhite
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Proceder al Pago",
                    color = ROGRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
