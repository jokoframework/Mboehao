# Mbo´ehao

Trabajo para relevar lista de funcionalidades que puedan ser reutilizadas en distintos proyectos Android.

## Que es Mbo´ehao?

Mbo`ehao, que en guaraní significa colegio, es un proyecto para realizar un relevamiento de la lista de pantallas y tecnologías de las aplicaciones Android, con el objetivo de identificar funcionalidades que pueda ser comunes a las aplicaciones Android, y puedan ser reutilizadas en otros proyectos.

## Lista de funcionalidades que se muestran en el proyecto

Login vía API Rest
Almacenamiento en MBAAS
Menu "hamburguer" lateral
Progress bar:
	Carga de contenido en activity
	Carga de una página web dentro de un webview
Fabric/Crashlytics
Cambio de contraseña modal con medida de fuerza de la contraseña
Pantalla EULA (aceptar licencia)
Pantalla modal con advertencia para medidas de seguridad
Gráfico (plot) XY, con bitmaps tipo puntos de interés (POI)
Compartir una imagen captura de sección de un activity (panel)
Ejecución de tareas periódicas
Uso de bases de datos SQLite con pattern tipo Service de Spring
	Copia mediante easter-egg del archivo de la base de datos
Uso de librería propia (bipolarlib) compartida como módulo Gradle y publicación a artifactory
Gamification (puntos y medallas)
Notificaciones tipo cortina superior deslizante (Crouton)
Logs Remotos vía API
Splay screen/About
Modal con información de ayuda
Noticias periódicas vía API (Parse server, pero generalizable)

## Funcionalidades proveídas por el proyecto hasta el momento

Se listan a continuación las funcionalidades básicas que ya pueden ser vistas en el repositorio:
-Login vía API Rest
-Almacenamiento en MBAAS
-Menu "hamburguer" lateral 
-Progress bar:
	Carga de contenido en activity login
-Logs Remotos vía API

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

