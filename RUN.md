### Step 1) Crear directorio:

    mkdir -p /opt/starter-kit/dev

### Step 2) Configuración del /opt/starter-kit/dev

Acceder y copiar el archivo "application.properties.example" y "development.vars":
    
    cd joko_backend_starter_kit/
    cp application.properties.example /opt/starter-kit/dev/
    cp development.vars /opt/starter-kit/
### Step 3) Configuración del archivo "development.vars"
Se debe configurar el archivo "development.vars", que servirá para la ejecucion de liquibase. Este es un archivo bash que debe tener dos variables:

    - MVN_SETTINGS: Archivo de configuración de perfil Maven. En caso de utilizar el Artifactory interno, sería el recién descargado. Ej: $HOME/.m2/settings.xml puede utilizar de ejemplo el archivo que se encuentra en src/main/resources/settings.xml.example copiando a $HOME/.m2/settings.xml
    - PROFILE_DIR: Directorio de perfil creado en el punto inicial. Ej. /opt/starter-kit
Un ejemplo de este archivo se encuentra en src/main/resources/development.vars.
Se recomienda que este archivo esté fuera del workspsace en el directorio padre de los PROFILE_DIR. Ejemplo: /opt/starter-kit/.


### Step 4) Acceder al directorio /opt/starter-kit/dev:

    cd /opt/starter-kit/dev
    mv application.properties.example application.properties
    cd /opt/starter-kit/

### Step 5) Configuración de variables de entorno:
-Editar el archivo: 
     nano development.vars 
-Exportar variable, desde la terminal:
  ```shell
    $ export ENV_VARS="/opt/starter-kit/development.vars"
  ```
  Obs.: El truco es tener varios archivos `profile.vars` y cada uno apuntando a
   un `PROFILE_DIR` diferente. 
### Step 6) Ejecutar Liquibase.
  
1.- Crea la schema de cero.
  ```shell
    $ ./scripts/updater fresh
  ```
2.- Inicializa datos básicos (o reinicializa)
  ```shell
    $ ./scripts/updater seed src/main/resources/db/sql/seed-data.sql
    $ ./scripts/updater seed src/main/resources/db/sql/seed-config.sql
  ```
  **OJO**:
* El parámetro `fresh` elimina la base de datos que está configurada en el `application.properties` y la vuelve a crear desde cero con la última versión del schema
  
* Los parámetros `seed <file>` cargan datos indicados en el archivo `<file>`, para los casos en que se ejecute `fresh` siempre debe ir seguido de un `seed` con el archivo que (re)inicializa los datos básicos del sistema 
  
* Los datos básicos del sistema estan en dos archivos:

`seed-data.sql`: Todos la configuracion base que es independiente al ambiente
`[ambiente]-config`. Por ejemplo: `dev-config.sql` . Posee los parametros de configuracion adecuados  para el ambiente de desarrollo. Tambien existe `qa-config` y `prod-config`
  
3.- Para correr el liquibase en modo de actualización ejecute: 
```
    $ ./scripts/updater update
  ```
    
## Corren con Maven

Una vez hechos estos cambios, solo debemos correr el proyecto como una 
aplicación de Spring Boot, o con la línea de comando (se requiere maven instalado).

```shell
  $ mvn spring-boot:run -Dext.prop.dir=/opt/starter-kit/dev -Dspring.config.location=file:///opt/starter-kit/dev/application.properties
```


El usuario/password default que se crea con la base de datos, es admin/123456

## Configuración de red dentro de Android Studio:
1.- Agregar un archivo "network_security_config" en res/xml.

2.- Agregar una configuración de dominio y establecer cleartextTrafficPermitted="true":
  ```shell
 <?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">yourip</domain>
    </domain-config>
</network-security-config>
  ```
3.- Agregar configuración de seguridad de red al archivo AndroidManifest.xml:
  ```shell
<application
    android:name=".MyApplication"
    android:networkSecurityConfig="@xml/network_security_config"
    ...
  ```
3.- Cambiar ip existente en jwt_URL y user_acces_URL dentro del archivo xml :
  ```shell
 <string name="jwt_URL">http://10.1.1.117:8080/api/login</string>
 <string name="user_acces_URL">http://10.1.1.117:8080/api/token/user-access</string>
  ```
