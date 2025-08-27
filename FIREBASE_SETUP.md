# 🔥 Configuración de Firebase - ASUS ROG Store

## 📋 Prerrequisitos

- Cuenta de Google
- Proyecto Android configurado
- Node.js instalado (para panel web)
- Conocimientos básicos de Firebase

## 🚀 Paso 1: Crear Proyecto Firebase

### 1.1 Acceder a Firebase Console
1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Inicia sesión con tu cuenta de Google
3. Haz clic en "Crear un proyecto"

### 1.2 Configurar Proyecto
```
Nombre del proyecto: ASUS-ROG-Store
ID del proyecto: asus-rog-store-xxxxx (se genera automáticamente)
Descripción: E-commerce multiplataforma para productos ASUS ROG
```

### 1.3 Configuración de Google Analytics
- ✅ Habilitar Google Analytics
- Cuenta de Analytics: Crear nueva cuenta
- Ubicación: Tu país
- Términos de servicio: Aceptar

## 🔐 Paso 2: Configurar Authentication

### 2.1 Habilitar Métodos de Autenticación
1. En Firebase Console, ve a **Authentication**
2. Haz clic en **Sign-in method**
3. Habilita los siguientes proveedores:

#### Email/Password
```
✅ Habilitar
✅ Requerir verificación de email
✅ Permitir contraseñas débiles (para desarrollo)
```

#### Google
```
✅ Habilitar
✅ Configurar OAuth consent screen
✅ Agregar dominios autorizados
```

### 2.2 Configurar OAuth Consent Screen
1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Selecciona tu proyecto Firebase
3. Ve a **APIs & Services** > **OAuth consent screen**
4. Configura:
   - App name: ASUS ROG Store
   - User support email: tu-email@ejemplo.com
   - Developer contact information: tu-email@ejemplo.com

### 2.3 Configurar Dominios Autorizados
```
Dominios autorizados:
- localhost
- tu-dominio.com
- admin.tu-dominio.com
```

## 🗄️ Paso 3: Configurar Firestore Database

### 3.1 Crear Base de Datos
1. En Firebase Console, ve a **Firestore Database**
2. Haz clic en **Crear base de datos**
3. Selecciona **Modo de producción** (para desarrollo usa modo de prueba)
4. Ubicación: Selecciona la más cercana a tus usuarios

### 3.2 Configurar Reglas de Seguridad
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
    
    // Categorías de solo lectura
    match /categorias/{categoriaId} {
      allow read: if true;
      allow write: if request.auth != null && 
        get(/databases/$(database)/documents/usuarios/$(request.auth.uid)).data.rol == 'admin';
    }
  }
}
```

### 3.3 Crear Colecciones Iniciales
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

## 📁 Paso 4: Configurar Storage

### 4.1 Habilitar Cloud Storage
1. En Firebase Console, ve a **Storage**
2. Haz clic en **Comenzar**
3. Selecciona **Modo de producción** (para desarrollo usa modo de prueba)
4. Ubicación: Misma que Firestore

### 4.2 Configurar Reglas de Storage
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Usuarios pueden subir imágenes de perfil
    match /usuarios/{userId}/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Productos solo para administradores
    match /productos/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null && 
        firestore.get(/databases/(default)/documents/usuarios/$(request.auth.uid)).data.rol == 'admin';
    }
    
    // Imágenes públicas
    match /public/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

## 📱 Paso 5: Configurar App Android

### 5.1 Descargar google-services.json
1. En Firebase Console, ve a **Project Settings**
2. En la sección **Your apps**, haz clic en el ícono de Android
3. Registra tu app:
   - Android package name: `com.example.rog_store`
   - App nickname: ASUS ROG Store
   - Debug signing certificate SHA-1: (opcional para desarrollo)
4. Descarga `google-services.json`

### 5.2 Colocar google-services.json
```
Coloca el archivo google-services.json en:
ROG_Store/app/google-services.json
```

### 5.3 Verificar build.gradle
El archivo `app/build.gradle.kts` ya tiene la configuración necesaria:
```kotlin
plugins {
    id("com.google.gms.google-services")
    // ... otros plugins
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    // ... otras dependencias
}
```

### 5.4 Verificar build.gradle del proyecto
El archivo `build.gradle.kts` del proyecto raíz ya tiene:
```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.0" apply false
}
```

## 🌐 Paso 6: Configurar Panel Web

### 6.1 Configurar Firebase en Next.js
El archivo `admin-panel/firebase-config.js` ya está configurado, pero necesitas actualizar con tu configuración:

```javascript
const firebaseConfig = {
  apiKey: "tu-api-key-real",
  authDomain: "tu-proyecto.firebaseapp.com",
  projectId: "tu-proyecto",
  storageBucket: "tu-proyecto.appspot.com",
  messagingSenderId: "123456789",
  appId: "tu-app-id"
};
```

### 6.2 Obtener Configuración
1. En Firebase Console, ve a **Project Settings**
2. En la sección **Your apps**, haz clic en **Add app** > **Web**
3. Registra tu app web
4. Copia la configuración

### 6.3 Variables de Entorno (Opcional)
Crea un archivo `.env.local` en `admin-panel/`:
```bash
NEXT_PUBLIC_FIREBASE_API_KEY=tu-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=tu-proyecto.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=tu-proyecto
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=tu-proyecto.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=123456789
NEXT_PUBLIC_FIREBASE_APP_ID=tu-app-id
```

## 🔔 Paso 7: Configurar Cloud Messaging

### 7.1 Habilitar FCM
1. En Firebase Console, ve a **Cloud Messaging**
2. Haz clic en **Comenzar**
3. Configura tu app Android

### 7.2 Configurar Android
1. Descarga `google-services.json` actualizado
2. Reemplaza el archivo existente
3. Agregar dependencia en `app/build.gradle.kts`:
```kotlin
implementation("com.google.firebase:firebase-messaging-ktx")
```

### 7.3 Configurar Web
1. En Firebase Console, ve a **Cloud Messaging**
2. En la pestaña **Web Push certificates**
3. Genera un nuevo certificado
4. Agrega a tu configuración web

## 🧪 Paso 8: Probar la Configuración

### 8.1 Probar App Android
1. Sincroniza el proyecto: `./gradlew clean build`
2. Ejecuta la app en un dispositivo/emulador
3. Verifica que no hay errores de Firebase
4. Prueba la autenticación

### 8.2 Probar Panel Web
1. Instala dependencias: `npm install`
2. Ejecuta en desarrollo: `npm run dev`
3. Abre http://localhost:3000
4. Verifica que se conecta a Firebase

### 8.3 Verificar en Firebase Console
1. **Authentication**: Usuarios registrados
2. **Firestore**: Datos creados
3. **Storage**: Archivos subidos
4. **Analytics**: Eventos registrados

## 🔒 Paso 9: Configuración de Seguridad

### 9.1 Reglas de Firestore
```javascript
// Reglas más restrictivas para producción
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Solo usuarios autenticados pueden acceder
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
    
    // Administradores tienen acceso completo
    match /{document=**} {
      allow read, write: if request.auth != null && 
        get(/databases/$(database)/documents/usuarios/$(request.auth.uid)).data.rol == 'admin';
    }
  }
}
```

### 9.2 Reglas de Storage
```javascript
// Reglas más restrictivas para producción
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Solo usuarios autenticados pueden subir archivos
    match /{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

### 9.3 Configuración de Authentication
- ✅ Requerir verificación de email
- ✅ Configurar contraseñas mínimas
- ✅ Habilitar bloqueo de cuenta después de intentos fallidos
- ✅ Configurar tiempo de expiración de sesión

## 📊 Paso 10: Configurar Analytics

### 10.1 Habilitar Google Analytics
1. En Firebase Console, ve a **Analytics**
2. Haz clic en **Comenzar**
3. Configura tu cuenta de Analytics

### 10.2 Configurar Eventos Personalizados
```javascript
// Eventos recomendados para e-commerce
- product_view
- add_to_cart
- purchase
- user_registration
- user_login
- search
- category_view
```

### 10.3 Configurar Conversiones
- Registro de usuario
- Agregar al carrito
- Compra completada
- Ver producto

## 🚀 Paso 11: Despliegue

### 11.1 Configuración de Producción
1. Cambia las reglas de Firestore a modo producción
2. Configura reglas de Storage más restrictivas
3. Habilita verificación de email obligatoria
4. Configura dominios autorizados para producción

### 11.2 Variables de Entorno de Producción
```bash
# Panel Web
NODE_ENV=production
NEXT_PUBLIC_FIREBASE_API_KEY=tu-api-key-produccion
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=tu-proyecto.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=tu-proyecto
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=tu-proyecto.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=123456789
NEXT_PUBLIC_FIREBASE_APP_ID=tu-app-id-produccion
```

### 11.3 Monitoreo
1. Configura alertas en Firebase Console
2. Monitorea logs de errores
3. Revisa métricas de rendimiento
4. Configura notificaciones de seguridad

## 🆘 Solución de Problemas Comunes

### Error: "Google Services plugin not found"
```bash
# Solución: Sincronizar proyecto
./gradlew clean build
```

### Error: "Firebase not initialized"
```bash
# Verificar que google-services.json esté en app/
# Verificar que el plugin esté en build.gradle
```

### Error: "Permission denied" en Firestore
```bash
# Verificar reglas de seguridad
# Verificar que el usuario esté autenticado
# Verificar rol de administrador
```

### Error: "Storage not initialized"
```bash
# Verificar configuración de Storage
# Verificar reglas de Storage
# Verificar permisos de archivo
```

## 📚 Recursos Adicionales

### Documentación Oficial
- [Firebase Documentation](https://firebase.google.com/docs)
- [Android Setup Guide](https://firebase.google.com/docs/android/setup)
- [Web Setup Guide](https://firebase.google.com/docs/web/setup)
- [Security Rules](https://firebase.google.com/docs/rules)

### Comunidad
- [Firebase Community](https://firebase.google.com/community)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/firebase)
- [Firebase YouTube Channel](https://www.youtube.com/user/Firebase)

### Herramientas
- [Firebase CLI](https://firebase.google.com/docs/cli)
- [Firebase Emulator](https://firebase.google.com/docs/emulator-suite)
- [Firebase Extensions](https://firebase.google.com/docs/extensions)

---

## ✅ Checklist de Configuración

- [ ] Proyecto Firebase creado
- [ ] Authentication configurado
- [ ] Firestore Database configurado
- [ ] Storage configurado
- [ ] Cloud Messaging configurado
- [ ] google-services.json descargado
- [ ] App Android configurada
- [ ] Panel web configurado
- [ ] Reglas de seguridad configuradas
- [ ] Analytics configurado
- [ ] Pruebas realizadas
- [ ] Configuración de producción lista

---

**¡Firebase está configurado correctamente! 🎉**

Tu proyecto ASUS ROG Store ahora tiene un backend robusto y escalable. Recuerda revisar regularmente las reglas de seguridad y mantener actualizadas las dependencias.
