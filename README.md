# ASUS ROG Store - E-commerce Multiplataforma

## 📱 Descripción del Proyecto

ASUS ROG Store es una aplicación de e-commerce multiplataforma desarrollada para la marca ASUS ROG, especializada en productos gaming. La aplicación incluye tanto una app móvil (Android/iOS) como un panel web de administración.

## 🎯 Características Principales

### App Móvil
- **Pantalla de inicio** con categorías de productos
- **Catálogo de productos** con filtros por categoría
- **Detalle de producto** con especificaciones técnicas
- **Carrito de compras** funcional
- **Perfil de usuario** con autenticación
- **Modo offline** con cache local
- **Diseño gamer** con tema ASUS ROG (rojo/negro)

### Panel Web de Administración
- **Gestión de productos** (CRUD completo)
- **Vista de usuarios** registrados
- **Monitoreo de pedidos**
- **Estadísticas** en tiempo real
- **Interfaz responsive** para administradores

## 🏗️ Arquitectura del Proyecto

### Patrón MVVM (Model-View-ViewModel)
- **Model**: Entidades de datos y repositorios
- **View**: Pantallas de Jetpack Compose
- **ViewModel**: Lógica de negocio y estado de la UI

### Tecnologías Utilizadas

#### App Móvil
- **Kotlin** con **Jetpack Compose**
- **Hilt** para inyección de dependencias
- **Room** para base de datos local
- **Firebase** (Auth, Firestore, Storage, Cloud Messaging)
- **Navigation Compose** para navegación
- **Coil** para carga de imágenes
- **Coroutines** para operaciones asíncronas

#### Panel Web
- **Next.js** con **React**
- **Material-UI** para componentes
- **Firebase** para backend
- **React Hook Form** para formularios

## 📁 Estructura del Proyecto

```
ROG_Store/
├── app/                                    # App Android
│   ├── src/main/
│   │   ├── java/com/example/rog_store/
│   │   │   ├── data/                       # Capa de datos
│   │   │   │   ├── local/                  # Base de datos Room
│   │   │   │   ├── model/                  # Modelos de datos
│   │   │   │   └── repository/             # Repositorios
│   │   │   ├── di/                         # Módulos Hilt
│   │   │   ├── ui/                         # Capa de presentación
│   │   │   │   ├── screens/                # Pantallas de la app
│   │   │   │   ├── theme/                  # Tema y estilos
│   │   │   │   └── navigation/             # Navegación
│   │   │   └── viewmodel/                  # ViewModels
│   │   └── res/                            # Recursos Android
│   └── build.gradle.kts                    # Configuración Gradle
├── admin-panel/                            # Panel web de administración
│   ├── pages/                              # Páginas Next.js
│   ├── firebase-config.js                  # Configuración Firebase
│   └── package.json                        # Dependencias Node.js
└── README.md                               # Documentación
```

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Node.js 16+ (para panel web)
- Cuenta de Firebase

### 1. Configuración de Firebase

#### Crear Proyecto Firebase
1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto
3. Habilita los siguientes servicios:
   - Authentication (Email/Password, Google)
   - Firestore Database
   - Storage
   - Cloud Messaging

#### Configurar Firestore
Crea las siguientes colecciones:

```javascript
// Colección: usuarios
{
  id_usuario: "auto-generated",
  nombre: "string",
  correo: "string",
  rol: "admin|cliente",
  createdAt: "timestamp",
  lastLogin: "timestamp"
}

// Colección: productos
{
  id_producto: "auto-generated",
  nombre: "string",
  precio: "number",
  categoria: "string",
  descripcion: "string",
  imagen_url: "string",
  specifications: "map",
  inStock: "boolean",
  rating: "number",
  reviewCount: "number",
  createdAt: "timestamp"
}

// Colección: pedidos
{
  id_pedido: "auto-generated",
  id_usuario: "string",
  items: "array",
  total: "number",
  status: "string",
  createdAt: "timestamp",
  shippingAddress: "string",
  paymentMethod: "string"
}

// Colección: categorias
{
  id_categoria: "auto-generated",
  nombre: "string",
  descripcion: "string",
  imagen_url: "string",
  productCount: "number"
}
```

#### Configurar Reglas de Seguridad Firestore
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Usuarios pueden leer/editar solo su propio perfil
    match /usuarios/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Productos son de solo lectura para usuarios
    match /productos/{productId} {
      allow read: if true;
      allow write: if request.auth != null && 
        get(/databases/$(database)/documents/usuarios/$(request.auth.uid)).data.rol == 'admin';
    }
    
    // Pedidos solo para usuarios autenticados
    match /pedidos/{orderId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### 2. Configuración de la App Android

#### Agregar google-services.json
1. Descarga `google-services.json` desde Firebase Console
2. Colócalo en `app/` del proyecto Android

#### Configurar Dependencias
Las dependencias ya están configuradas en `app/build.gradle.kts`:
- Firebase BOM
- Jetpack Compose
- Hilt
- Room
- Navigation
- Coil

#### Sincronizar Proyecto
```bash
./gradlew clean build
```

### 3. Configuración del Panel Web

#### Instalar Dependencias
```bash
cd admin-panel
npm install
```

#### Configurar Firebase
1. Actualiza `firebase-config.js` con tu configuración
2. Configura las variables de entorno si es necesario

#### Ejecutar Panel
```bash
npm run dev
```

## 🎨 Tema y Estilo Visual

### Paleta de Colores ASUS ROG
- **ROGRed**: #FF0000 (Rojo principal)
- **ROGDarkRed**: #CC0000 (Rojo oscuro)
- **ROGBlack**: #000000 (Negro)
- **ROGDarkGray**: #1A1A1A (Gris oscuro)
- **ROGGray**: #2A2A2A (Gris medio)
- **ROGWhite**: #FFFFFF (Blanco)
- **ROGNeonRed**: #FF1A1A (Rojo neón)
- **ROGAccent**: #00FFFF (Cian acento)

### Características del Diseño
- Gradientes rojo/negro
- Efectos neón en botones
- Bordes redondeados
- Tipografía moderna
- Iconografía Material Design

## 📱 Funcionalidades de la App

### Pantalla de Inicio
- Header con logo ASUS ROG
- Barra de búsqueda
- Categorías de productos
- Lista de productos destacados

### Detalle de Producto
- Imagen del producto
- Información básica (nombre, precio, rating)
- Especificaciones técnicas
- Descripción completa
- Controles de cantidad
- Botón "Agregar al Carrito"

### Carrito de Compras
- Lista de productos seleccionados
- Controles de cantidad
- Cálculo de subtotales
- Total general
- Botón "Proceder al Pago"

### Perfil de Usuario
- Estado de autenticación
- Formulario de login
- Información del usuario
- Opciones de perfil
- Historial de compras

## 🖥️ Funcionalidades del Panel Web

### Dashboard
- Estadísticas en tiempo real
- Contadores de productos, usuarios y pedidos
- Acceso rápido a funciones principales

### Gestión de Productos
- Lista de productos existentes
- Formulario para agregar/editar productos
- Eliminación de productos
- Filtros y búsqueda

### Gestión de Usuarios
- Vista de usuarios registrados
- Información de perfiles
- Roles y permisos

### Monitoreo de Pedidos
- Lista de pedidos
- Estados de pedidos
- Información de clientes

## 🔧 Configuración de Desarrollo

### Variables de Entorno
```bash
# Firebase
FIREBASE_API_KEY=tu-api-key
FIREBASE_AUTH_DOMAIN=tu-proyecto.firebaseapp.com
FIREBASE_PROJECT_ID=tu-proyecto
FIREBASE_STORAGE_BUCKET=tu-proyecto.appspot.com
FIREBASE_MESSAGING_SENDER_ID=123456789
FIREBASE_APP_ID=tu-app-id
```

### Comandos de Desarrollo

#### App Android
```bash
# Limpiar y construir
./gradlew clean build

# Instalar en dispositivo
./gradlew installDebug

# Ejecutar tests
./gradlew test
```

#### Panel Web
```bash
# Modo desarrollo
npm run dev

# Construir para producción
npm run build

# Ejecutar producción
npm start
```

## 🧪 Testing

### App Android
- **Unit Tests**: JUnit para ViewModels y Repositorios
- **UI Tests**: Espresso para pantallas
- **Integration Tests**: Pruebas de base de datos

### Panel Web
- **Unit Tests**: Jest para componentes
- **E2E Tests**: Cypress para flujos completos

## 📦 Despliegue

### App Android
1. Generar APK firmado
2. Subir a Google Play Store
3. Configurar Firebase App Distribution para testing

### Panel Web
1. Construir proyecto: `npm run build`
2. Desplegar en Vercel, Netlify o servidor propio
3. Configurar dominio personalizado

## 🔒 Seguridad

### Autenticación
- Firebase Authentication
- Roles de usuario (admin/cliente)
- Validación de permisos

### Base de Datos
- Reglas de seguridad Firestore
- Validación de datos
- Sanitización de inputs

### API
- Rate limiting
- Validación de tokens
- HTTPS obligatorio

## 📊 Monitoreo y Analytics

### Firebase Analytics
- Eventos de usuario
- Conversiones
- Retención

### Crashlytics
- Reportes de errores
- Stack traces
- Información del dispositivo

## 🤝 Contribución

### Estándares de Código
- **Kotlin**: Ktlint + detekt
- **JavaScript**: ESLint + Prettier
- **Commits**: Conventional Commits

### Flujo de Trabajo
1. Fork del repositorio
2. Crear rama feature
3. Implementar cambios
4. Ejecutar tests
5. Crear Pull Request

## 📚 Recursos Adicionales

### Documentación
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/docs)
- [Material Design](https://material.io/design)
- [ASUS ROG Brand Guidelines](https://rog.asus.com/)

### Herramientas
- [Android Studio](https://developer.android.com/studio)
- [Firebase Console](https://console.firebase.google.com/)
- [Postman](https://www.postman.com/) para testing de APIs

## 📞 Soporte

### Contacto del Equipo
- **Desarrollador Principal**: [Tu Nombre]
- **Email**: [tu-email@ejemplo.com]
- **GitHub**: [tu-usuario]

### Reportar Issues
1. Verificar que no sea un issue duplicado
2. Proporcionar información del dispositivo
3. Incluir logs de error
4. Describir pasos para reproducir

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

## 🙏 Agradecimientos

- **ASUS ROG** por la inspiración y branding
- **Google** por Firebase y Android
- **JetBrains** por Kotlin
- **Vercel** por Next.js hosting
- **Comunidad open source** por las librerías utilizadas

---

**ASUS ROG Store** - Gaming Gear for Champions 🎮⚡
