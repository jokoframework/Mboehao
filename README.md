# Mbo´ehao

Trabajo para relevar lista de funcionalidades que puedan ser reutilizadas en distintos proyectos Android.

## Que es Mbo´ehao?

Mbo`ehao, que en guaraní significa colegio, es un proyecto para realizar un relevamiento de la lista de pantallas y tecnologías de las aplicaciones Android, con el objetivo de identificar funcionalidades que pueda ser comunes a las aplicaciones Android, y puedan ser reutilizadas en otros proyectos.

## Lista de funcionalidades que se pretenden mostrar en el proyecto

- Login vía API Rest
- Selección de SERVIDOR antes del Login
- Login con FaceBook y Twitter (Amazon Cognito)
- Login con JWT
- Almacenamiento en MBAAS
- Logs Remotos vía API
- Menu "hamburguer" lateral
- Progress bar:
	- Carga de contenido en activity
	- Carga de una página web dentro de un webview
- Fabric/Crashlytics
- Fabric en modo release
- Cambio de contraseña modal con medida de fuerza de la contraseña
- Pantalla EULA (aceptar licencia)
- Pantalla modal con advertencia para medidas de seguridad
- Gráfico MPAndroidChart
- Compartir una imagen captura de sección de un activity (panel)
- Ejecución de tareas periódicas
- Uso de bases de datos SQLite con pattern tipo Service de Spring
	- Copia mediante easter-egg del archivo de la base de datos
- Uso de librería propia (mboehaolib) compartida como módulo Gradle y publicación a artifactory
- Gamification (puntos y medallas)
- Notificaciones tipo cortina superior deslizante (Crouton)
- Manejo de notificaciones dentro de la propia APP
- Integración con Notification Server
- Display screen/About
- Modal con información de ayuda
- Noticias periódicas vía API (Parse server, pero generalizable)
- Recycler / Card view
- Google Analytics
- Lista seleccionable (ListDialog)
- Filtro TimeLine
- SQLite para guardar datos localmente 
- Extracción de datos desde Menú logueado y deslogueado con combinación de teclas
- Manejo de pestañas o tabs
- Listado con agrupación por proyecto de formularios y documentos
- Manejo de atributos en el header para ser multitenant
- Custom Share a redes sociales con conteo de estadísticas
- Buzón de entrada de tareas asignadas

## Funcionalidades proveídas por el proyecto hasta el momento

Se listan a continuación las funcionalidades básicas que ya pueden ser vistas en el repositorio:
- Login vía API Rest
- Login con FaceBook
- Login con JWT
- Almacenamiento en MBAAS
- Logs Remotos vía API
- Menu "hamburguer" lateral
- Progress bar:
	- Carga de contenido en activity
	- Carga de una página web dentro de un webview
- Fabric/Crashlytics
- Cambio de contraseña modal con medida de fuerza de la contraseña
- Pantalla EULA (aceptar licencia)
- Pantalla modal con advertencia para medidas de seguridad
- Gráfico MPAndroidChart
- Compartir una imagen captura de sección de un activity (panel)
- Ejecución de tareas periódicas
- Uso de librería propia (mboehaolib) compartida como módulo Gradle
- Notificaciones tipo cortina superior deslizante (Crouton)
- Manejo de notificaciones dentro de la propia APP
- Integración con Notification Server(Firebase)
- Display screen/About
- Noticias periódicas vía API (Parse server, pero generalizable)
- Google Analytics(Firebase)
- Uso del PreferencesActvity para configuraciones minimas de la APP.

## Parse

El Parse es un servidor BAAS, que en este caso se maneja como MBAAS por el tipo de proyecto. Si se necesitan alguna guia para el manejo de lo que es el servidor, se puede
mirar la [guia](http://docs.parseplatform.org/android/guide/) para tener una mejor idea. 

### Parse Dashboard

El Dashboar es la informacion que guarda la aplicacion en nuestro servidor Parse.
Nos muestra los usuarios, logs que se realizaron dependiendo de las acciones, y muchas cosas que se quieran y/o necesiten para el usuario.
Por el momento la aplicacion solo guarda los logs como una forma de muestra de funcionalidades de logs remotos y alamacenamiento MBAAS.
La idea es justamente tener este proyecto como base para agregar nuevas ideas.

## SonarRunner Tests

Mbo`ehao cuenta con un archivo de configuracion para poder realizar analisis con el sonar de ser necesarios.
Para dicho proceso se puede seguir la siguiente [guide](https://androidresearch.wordpress.com/2014/05/29/analysing-android-code-with-sonarqube/)

## Code Style

El proyecto siguee las convenciones del [Android Code Style Guidelines](http://source.android.com/source/code-style.html).

## MediaWiki

Este proyecto tiene una documentacion via Wiki, donde se va detallando con mayor precision las funcionalidades e implementaciones, y algunas referencias más
que se pueden observar al llevar a cabo el proyecto. [Wiki Project](https://joko.miraheze.org/wiki/Mbo%60ehao).
