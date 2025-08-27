package com.example.rog_store.data.repository

import com.example.rog_store.data.local.ProductDao
import com.example.rog_store.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao
) {
    
    // Obtener productos desde Firebase
    suspend fun fetchProductsFromFirebase(): List<Product> {
        return try {
            val snapshot = firestore.collection("productos").get().await()
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            // Guardar en base de datos local
            productDao.insertProducts(products)
            products
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Obtener productos por categoría desde Firebase
    suspend fun fetchProductsByCategoryFromFirebase(category: String): List<Product> {
        return try {
            val snapshot = firestore.collection("productos")
                .whereEqualTo("category", category)
                .get()
                .await()
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            // Guardar en base de datos local
            productDao.insertProducts(products)
            products
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Obtener productos desde base de datos local
    fun getProductsFromLocal(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }
    
    // Obtener productos por categoría desde base de datos local
    fun getProductsByCategoryFromLocal(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }
    
    // Buscar productos
    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }
    
    // Obtener producto por ID
    suspend fun getProductById(productId: String): Product? {
        return productDao.getProductById(productId)
    }
    
    // Agregar producto (solo para admin)
    suspend fun addProduct(product: Product): Boolean {
        return try {
            val docRef = firestore.collection("productos").add(product).await()
            val productWithId = product.copy(id = docRef.id)
            productDao.insertProduct(productWithId)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Actualizar producto (solo para admin)
    suspend fun updateProduct(product: Product): Boolean {
        return try {
            firestore.collection("productos").document(product.id).set(product).await()
            productDao.updateProduct(product)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Eliminar producto (solo para admin)
    suspend fun deleteProduct(productId: String): Boolean {
        return try {
            firestore.collection("productos").document(productId).delete().await()
            val product = productDao.getProductById(productId)
            product?.let { productDao.deleteProduct(it) }
            true
        } catch (e: Exception) {
            false
        }
    }
}
