package com.example.rog_store.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rog_store.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    
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
        ProfileHeader()
        
        if (isLoggedIn) {
            UserProfile(
                onLogout = { isLoggedIn = false }
            )
        } else {
            GuestProfile(
                onLoginClick = { showLoginDialog = true }
            )
        }
    }
    
    if (showLoginDialog) {
        LoginDialog(
            onDismiss = { showLoginDialog = false },
            onLoginSuccess = {
                isLoggedIn = true
                showLoginDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader() {
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
            text = "Mi Perfil ROG",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite
        )
    }
}

@Composable
fun GuestProfile(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = ROGGray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Inicia sesión en tu cuenta ROG",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ROGWhite,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Accede a tu historial de compras, guarda productos favoritos y recibe ofertas exclusivas",
            fontSize = 16.sp,
            color = ROGGray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ROGRed
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Login,
                contentDescription = null,
                tint = ROGWhite
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Iniciar Sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { /* TODO: Implementar registro */ },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            colors = OutlinedButtonDefaults.colors(
                border = ROGRed,
                contentColor = ROGRed
            )
        ) {
            Icon(
                Icons.Default.PersonAdd,
                contentDescription = null,
                tint = ROGRed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Crear Cuenta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun UserProfile(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Información del usuario
        Card(
            colors = CardDefaults.cardColors(
                containerColor = ROGDarkGray
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = ROGRed
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "Usuario ROG",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ROGWhite
                        )
                        Text(
                            text = "usuario@rog.com",
                            fontSize = 14.sp,
                            color = ROGGray
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Opciones del perfil
        ProfileOption(
            icon = Icons.Default.ShoppingCart,
            title = "Historial de Compras",
            subtitle = "Ver tus pedidos anteriores",
            onClick = { /* TODO: Navegar a historial */ }
        )
        
        ProfileOption(
            icon = Icons.Default.Favorite,
            title = "Productos Favoritos",
            subtitle = "Productos que te gustan",
            onClick = { /* TODO: Navegar a favoritos */ }
        )
        
        ProfileOption(
            icon = Icons.Default.Notifications,
            title = "Notificaciones",
            subtitle = "Configurar alertas y ofertas",
            onClick = { /* TODO: Configurar notificaciones */ }
        )
        
        ProfileOption(
            icon = Icons.Default.Settings,
            title = "Configuración",
            subtitle = "Preferencias de la cuenta",
            onClick = { /* TODO: Navegar a configuración */ }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Botón de cerrar sesión
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = ROGNeonPink
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                Icons.Default.Logout,
                contentDescription = null,
                tint = ROGWhite
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar Sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = ROGGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = ROGRed,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = ROGWhite
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = ROGGray
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ROGGray
            )
        }
    }
}

@Composable
fun LoginDialog(
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Iniciar Sesión",
                color = ROGWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ROGRed,
                        unfocusedBorderColor = ROGGray
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ROGRed,
                        unfocusedBorderColor = ROGGray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    // TODO: Implementar autenticación real
                    // Simular delay de login
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                        kotlinx.coroutines.delay(1000)
                        isLoading = false
                        onLoginSuccess()
                    }
                },
                enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ROGRed
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = ROGWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = OutlinedButtonDefaults.colors(
                    border = ROGGray,
                    contentColor = ROGGray
                )
            ) {
                Text("Cancelar")
            }
        },
        containerColor = ROGDarkGray,
        titleContentColor = ROGWhite,
        textContentColor = ROGWhite
    )
}
