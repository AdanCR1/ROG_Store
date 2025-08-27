package com.example.rog_store.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import com.example.rog_store.data.model.Product
import com.example.rog_store.ui.theme.*
import com.example.rog_store.ui.viewmodel.CartViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    // TODO: Obtener producto real desde ViewModel
    val product = remember {
        Product(
            id = productId,
            name = "ROG Strix G15 Gaming Laptop",
            price = 1299.99,
            category = "Computadoras",
            description = "Laptop gaming de alto rendimiento con procesador AMD Ryzen 7, GPU NVIDIA RTX 3060, 16GB RAM y SSD de 512GB. Diseñada para gamers que buscan el máximo rendimiento.",
            imageUrl = "",
            specifications = mapOf(
                "Procesador" to "AMD Ryzen 7 5800H",
                "GPU" to "NVIDIA RTX 3060 6GB",
                "RAM" to "16GB DDR4",
                "Almacenamiento" to "512GB SSD NVMe",
                "Pantalla" to "15.6\" FHD 144Hz",
                "Sistema Operativo" to "Windows 11 Home"
            ),
            rating = 4.8f,
            reviewCount = 127
        )
    }
    
    var quantity by remember { mutableStateOf(1) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ROGBlack, ROGDarkGray)
                )
            )
    ) {
        // Header con botón de regreso
        ProductDetailHeader(
            onBackClick = { navController.navigateUp() }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                // Imagen del producto
                ProductImage(product = product)
                
                // Información básica
                ProductBasicInfo(product = product)
                
                // Especificaciones técnicas
                ProductSpecifications(product = product)
                
                // Descripción
                ProductDescription(product = product)
                
                // Controles de cantidad y agregar al carrito
                ProductActions(
                    product = product,
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    onAddToCart = {
                        cartViewModel.addToCart(product, quantity)
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun ProductDetailHeader(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Detalle del Producto", color = ROGWhite) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = ROGWhite
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ROGDarkGray
        )
    )
}

@Composable
fun ProductImage(product: Product) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = product.imageUrl.ifEmpty { R.drawable.placeholder_product },
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        
        // Badge de calificación
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = ROGRed.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = ROGWhite,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating}",
                    color = ROGWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProductBasicInfo(product: Product) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            textAlign = TextAlign.Start
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "$${product.price}",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = ROGRed
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "${product.reviewCount} reseñas",
            fontSize = 14.sp,
            color = ROGGray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductSpecifications(product: Product) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Especificaciones Técnicas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = ROGGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                product.specifications.forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = key,
                            color = ROGGray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = value,
                            color = ROGWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    if (key != product.specifications.keys.last()) {
                        Divider(
                            color = ROGDarkGray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductDescription(product: Product) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Descripción",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Text(
            text = product.description,
            color = ROGWhite,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductActions(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Selector de cantidad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cantidad:",
                color = ROGWhite,
                fontSize = 16.sp
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }
                ) {
                    Text(
                        text = "-",
                        color = ROGRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = quantity.toString(),
                    color = ROGWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                IconButton(
                    onClick = { onQuantityChange(quantity + 1) }
                ) {
                    Text(
                        text = "+",
                        color = ROGRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón agregar al carrito
        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ROGRed
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = ROGWhite
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar al Carrito - $${product.price * quantity}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
