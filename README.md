# ✉️ Aplicación de Chat con Firebase

## Descripción
Esta es una aplicación de chat en tiempo real desarrollada en **Kotlin** para Android, utilizando **Firebase Realtime Database** para la sincronización de mensajes y **Firebase Authentication** para la autenticación de usuarios mediante Google Sign-In.

## Características
- Inicio de sesión con Google.
- Envio y recepción de mensajes en tiempo real.
- Interfaz con **Material Design**.
- Historial de mensajes persistente en Firebase Realtime Database.
- Posibilidad de eliminar mensajes enviados por el usuario.
- Opción de borrar todos los mensajes de un usuario sin afectar a los de otros.
- Navegación mediante **DrawerLayout** con opciones para cerrar sesión o salir de la app.

## Tecnologías utilizadas
- **Kotlin**
- **Android Studio**
- **Firebase Realtime Database**
- **Firebase Authentication**
- **Material Design Components**
- **RecyclerView** para la lista de mensajes

## Instalación y Configuración
1. **Clonar el repositorio**
   ```sh
   git clone https://github.com/tuusuario/chat-firebase-android.git
   ```
2. **Abrir el proyecto en Android Studio**
3. **Configurar Firebase**
   - Crear un nuevo proyecto en [Firebase Console](https://console.firebase.google.com/).
   - Agregar una aplicación Android y descargar el archivo `google-services.json`.
   - Colocar el archivo en la carpeta `app` del proyecto.
   - Habilitar Authentication -> Sign-in method -> Google.
   - Habilitar Firebase Realtime Database y establecer reglas de lectura/escritura.

## Uso
1. Abrir la aplicación y hacer login con una cuenta de Google.
2. Enviar mensajes desde la caja de texto en la parte inferior.
3. Los mensajes se mostrarán en un **RecyclerView**, diferenciando los del usuario logueado de los demás.
4. El usuario puede eliminar sus propios mensajes o borrar todos sus mensajes desde el menú lateral.

## Autor
Desarrollado por **Javier Palmero Clares**.
