package com.example.rog_store.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rog_store.R
import com.example.rog_store.data.model.Category
import com.example.rog_store.data.model.Product
import com.example.rog_store.ui.theme.*
import com.example.rog_store.ui.viewmodel.ProductViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val products by productViewModel.products.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    val categories = listOf(
        Category("1", "Computadoras", "Laptops y PCs gaming", "", 0),
        Category("2", "Smartphones", "Teléfonos gaming", "", 0),
        Category("3", "Consolas Portátiles", "Nintendo Switch y más", "", 0),
        Category("4", "Accesorios", "Periféricos gaming", "", 0)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ROGBlack, ROGDarkGray)
                )
            )
    ) {
        // Header con logo ROG
        HeaderSection()
        
        // Barra de búsqueda
        SearchBar(
            query = searchQuery,
            onQueryChange = { 
                searchQuery = it
                productViewModel.searchProducts(it)
            }
        )
        
        // Categorías
        CategoriesSection(
            categories = categories,
            onCategoryClick = { category ->
                productViewModel.loadProductsByCategory(category.name)
            }
        )
        
        // Productos
        ProductsSection(
            products = products,
            onProductClick = { product ->
                navController.navigate("product_detail/${product.id}")
            },
            onAddToCart = { product ->
                // TODO: Implementar agregar al carrito
            }
        )
        
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = ROGRed
            )
        }
        
        uiState.error?.let { error ->
            Text(
                text = error,
                color = ROGNeonPink,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(ROGRed, ROGDarkRed)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ASUS ROG STORE",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar productos ROG...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ROGRed,
            unfocusedBorderColor = ROGGray,
            focusedLabelColor = ROGRed,
            unfocusedLabelColor = ROGGray
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Categorías",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = ROGGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name,
                color = ROGWhite,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ProductsSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Productos Destacados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onAddToCart = { onAddToCart(product) }
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                model = product.imageUrl.ifEmpty { R.drawable.placeholder_product },
                contentDescription = product.name,
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
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ROGWhite
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$${product.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ROGRed
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ROGRed
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = ROGWhite
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }
    }
}
